package reportdao.util;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;

/**
 * <pre>
 * 代表一个EXCEl的某个工作表，一般情况是数据加载进来后，
 * 通过{@link #getAllDataRows()}去获取这个excel工作表
 * 的所有数据行，然后通过遍历数据行，根据{@link KGameExcelRow#getData(String)}
 * 等方法去获取数据。具体请参考{@link KGameExcelRow}
 * </pre>
 * 
 * @author PERRY CHAN
 */
public class KGameExcelTable {
	
	private String _tableName;					//数据表的名字
	private List<String> _headerNames;			//所有的标题的名字
	private KGameExcelRow[] _allDataRows;		//所有的数据行
	private int _rowCount;						//数据行的数量

//	/**
//	 * 加载excel数据
//	 * @param fullPath		excel的完整路径（可以是相对路径）
//	 * @param sheetName		需要加载的工作表名字
//	 * @param headerIndex	标题的索引
//	 * @throws IOException
//	 * @throws BiffException
//	 */
//	 void loadExcelData(String fullPath, String sheetName,
//			int headerIndex) throws IOException, BiffException {
//		Workbook wb = Workbook.getWorkbook(new File(fullPath));
//		Sheet sheet = wb.getSheet(sheetName);
//		if(sheet == null){
//			String[] sheetNames = wb.getSheetNames();
//			String currentName;
//			for(int i = 0; i < sheetNames.length; i++){
//				currentName = sheetNames[i];
//				if(currentName.toLowerCase().equals(sheetName.toLowerCase())){
//					sheet = wb.getSheet(currentName);
//					break;
//				}
//			}
//		}
//		this.loadData(sheet, headerIndex);
//	}
	
	/**
	 * 加载工作表的数据
	 * 
	 * @param sheet 指定的工作表
	 * @param headerIndex 标题行的索引
	 */
	void loadData(Sheet sheet, int headerIndex) {
		headerIndex = headerIndex - 1;
		_rowCount = sheet.getRows();
		if (headerIndex >= _rowCount) {
			throw new IllegalArgumentException("标题的索引[" + headerIndex + "]比数据的总行数[" + _rowCount + "]大！");
		}
		this._tableName = sheet.getName();
		Cell[] headerCells = sheet.getRow(headerIndex);
		_headerNames = new ArrayList<String>(headerCells.length);
		for (int i = 0; i < headerCells.length; i++) {
			_headerNames.add(headerCells[i].getContents());
		}
		_rowCount -= (headerIndex + 1);
		List<KGameExcelRow> list = new ArrayList<KGameExcelRow>();
		// this._allDataRows = new KGameExcelRow[_rowCount];
		int index = headerIndex + 1;
		int indexInFile = index + 1;
		Cell[] currentRow;
		KGameExcelRow row;
		int rowIndex = 0;
		for (int i = 0; i < _rowCount; i++) {
			currentRow = sheet.getRow(index);
			if (validateRow(currentRow)) {
				row = new KGameExcelRow(rowIndex, indexInFile);
				row.setTable(this);
				row.loadData(currentRow);
				list.add(row);
				rowIndex++;
			}
			indexInFile++;
			index++;
		}
		this._rowCount = rowIndex;
		this._allDataRows = new KGameExcelRow[list.size()];
		list.toArray(this._allDataRows);
	}
	
	private boolean validateRow(Cell[] row){
		for (int i = 0; i < row.length; i++){
			if(row[i] != null && row[i].getContents().length() > 0){
				return true;
			}
		}
		return false;
	}

	private int getColIndex(String colName) {
		return this._headerNames.indexOf(colName);
	}

	public String getTableName() {
		return this._tableName;
	}
	
	public KGameExcelRow[] getAllDataRows(){
		return this._allDataRows;
	}
	
	/**
	 * <pre>
	 * 检查工作表是否包含指定名字的列
	 * </pre>
	 * @param colName
	 * @return
	 */
	public boolean validateContainsColName(String colName){
		return this.getColIndex(colName) > 0;
	}
	
	/**
	 * 获取指定索引所在的行的实例
	 * @param rowIndex
	 * @return
	 */
	public KGameExcelRow getDataRow(int rowIndex){
		if(rowIndex < 0 || rowIndex > _allDataRows.length){
			throw new IllegalArgumentException("查找的索引[" + rowIndex + "]超出数据的范围[0] ~ " + "[" + _allDataRows.length + "]");
		}
		return _allDataRows[rowIndex];
	}

	/**
	 * <pre>
	 * 表示对excel一行数据的封装，提供通过列索引或者列名字获取这行数据
	 * 某个单元格数据的方法，{@link #getData(int)}，{@link #getData(String)}，
	 * 原始数据将会以String的数据类型返回。如果明确知道这一行的数据类型，可以通过
	 * 调用不同的获取方法去获取指定的数据类型的数据，例如{@link #getBoolean(int)}，
	 * 或者{@link #getFloat(String)}等等。每个基本类型的数据获取，均提供通过索引或
	 * 名字获取的方式。
	 * </pre>
	 * 
	 * @author PERRY CHAN
	 *
	 */
	public static class KGameExcelRow {
		private int _indexInFile; // 文件中的行号，从1开始计算
		private int _index;	//本行的索引
		private String[] _datas; //本行的所有数据
		private KGameExcelTable _table; //本行所在的数据表对象
		
		private static final String _EMPTY_DATA = "";

		public KGameExcelRow(int index, int indexInFile) {
			this._index = index;
			this._indexInFile = indexInFile;
		}
		
		void setTable(KGameExcelTable pTable){
			this._table = pTable;
		}

		void loadData(Cell[] datas) {
			try {
				this._datas = new String[this._table._headerNames.size()];
				int index = 0;
				int length = this._datas.length < datas.length ? this._datas.length : datas.length;
				Cell current;
				for (; index < length; index++) {
					current = datas[index];
					this._datas[index] = current.getContents().trim();
				}
				if (index < this._datas.length) {
					for (; index < this._datas.length; index++) {
						this._datas[index] = _EMPTY_DATA;
					}
				}
			} catch (RuntimeException e){
				System.err.println("-_-|| -_-|| -_-|| -_-|| 加载数据出错，当前行数：" + _indexInFile + " -_-|| -_-|| -_-|| -_-||");
				throw e;
			}
			
		}

		/**
		 * <pre>
		 * 通过列的索引，获取该单元格字符串类型的数据
		 * </pre>
		 * @param colIndex
		 * @return
		 */
		public String getData(int colIndex) {
			if (colIndex >= this._datas.length || colIndex < 0) {
				throw new ArrayIndexOutOfBoundsException("查找的索引[" + colIndex + "]超出数据的范围[0] ~ [" + (_datas.length - 1) + "]！当前列：" + this._index);
			}
			return this._datas[colIndex];
		}

		/**
		 * <pre>
		 * 通过列的名字，获取字符串格式的数据
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public String getData(String colName) {
			int index = this.getColIndexByColName(colName);
			return this.getData(index);
		}

		/**
		 * 获取本行相对于标题行的索引
		 * @return
		 */
		public int getIndex() {
			return this._index;
		}
		
		/**
		 * 
		 * 获取本行在excel文件中的行号，从1开始计算
		 * 
		 * @return
		 */
		public int getIndexInFile(){
			return this._indexInFile;
		}

		private int getColIndexByColName(String colName) {
			int index = this._table.getColIndex(colName);
			if(index == -1) {
				throw new RuntimeException("找不到指定的列：" + colName);
			}
			return index;
		}

		/**
		 * <pre>
		 * 以整型的数据类型返回指定索引的单元格的数据
		 * </pre>
		 * 
		 * @param colIndex
		 * @return
		 */
		public int getInt(int colIndex) {
			String data = this.getData(colIndex);
			return Integer.parseInt(data);
		}

		/**
		 * <pre>
		 * 以整型的数据类型返回指定名字的单元格的数据
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public int getInt(String colName) {
			int colIndex = this.getColIndexByColName(colName);
			return this.getInt(colIndex);
		}

		/**
		 * <pre>
		 * 以布尔值（boolean）的数据类型返回指定索引的单元格的数据，
		 * 当单元格的数据为数字时，1表示true，其他表示false。此方法会
		 * 把单元格的字符串以小写的形式进行转换
		 * </pre>
		 * 
		 * @param colIndex
		 * @return
		 */
		public boolean getBoolean(int colIndex) {
			String data = this.getData(colIndex);
			if (data.equals("1")) {
				return true;
			} else if (data.equals("0")) {
				return false;
			} else {
				return Boolean.parseBoolean(data.toLowerCase());
			}
		}

		/**
		 * <pre>
		 * 以布尔值（boolean）的数据类型返回指定列名的单元格的数据，
		 * 当单元格的数据为数字时，1表示true，其他表示false。此方法会
		 * 把单元格的字符串以小写的形式进行转换
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public boolean getBoolean(String colName) {
			int index = this.getColIndexByColName(colName);
			return this.getBoolean(index);
		}

		/**
		 * <pre>
		 * 以byte的数据类型返回指定索引的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public byte getByte(int colIndex) {
			String data = this.getData(colIndex);
			return Byte.parseByte(data);
		}

		/**
		 * <pre>
		 * 以byte的数据类型返回指定列名的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public byte getByte(String colName) {
			int colIndex = this.getColIndexByColName(colName);
			return this.getByte(colIndex);
		}
		
		/**
		 * <pre>
		 * 以整型的数据类型返回指定索引的单元格的数据
		 * </pre>
		 * 
		 * @param colIndex
		 * @return
		 */
		public short getShort(int colIndex) {
			String data = this.getData(colIndex);
			return Short.parseShort(data);
		}

		/**
		 * <pre>
		 * 以整型的数据类型返回指定名字的单元格的数据
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public short getShort(String colName) {
			int colIndex = this.getColIndexByColName(colName);
			return this.getShort(colIndex);
		}
		
		/**
		 * <pre>
		 * 以float的数据类型返回指定索引的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public float getFloat(int colIndex){
			String data = this.getData(colIndex);
			return Float.parseFloat(data);
		}
		
		/**
		 * <pre>
		 * 以float的数据类型返回指定列名的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public float getFloat(String colName){
			int colIndex = this.getColIndexByColName(colName);
			return this.getFloat(colIndex);
		}
		
		/**
		 * <pre>
		 * 以double的数据类型返回指定索引的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public double getDouble(int colIndex){
			String data = this.getData(colIndex);
			return Double.parseDouble(data);
		}
		
		/**
		 * <pre>
		 * 以double的数据类型返回指定列名的单元格的数据。
		 * </pre>
		 * 
		 * @param colName
		 * @return
		 */
		public double getDouble(String colName){
			int colIndex = this.getColIndexByColName(colName);
			return this.getDouble(colIndex);
		}
		
		public boolean containsCol(String colName) {
			int index = this._table.getColIndex(colName);
			if(index < 0) {
				return false;
			}
			return true;
		}
	}
	
	public static void main(String[] args) throws Exception {
		KGameExcelFile file = new KGameExcelFile("D:\\temp\\skillStatus.xls");
		KGameExcelTable table = file.getTable("技能状态（效果）表", 5);
//		KGameExcelRow[] allDatas = table.getAllDataRows();
//		System.out.println(allDatas.length);
//		System.out.println(allDatas[0].getData(0));
//		System.out.println(allDatas[0].getData("类型"));
//		System.out.println(allDatas[0].getData("移动速度"));
//		System.out.println(allDatas[1].getData("技能"));
//		System.out.println(allDatas[2].getData("名字"));
//		System.out.println(allDatas[3].getData("进化名字"));
		for(int i = 0; i < table._headerNames.size(); i++){
			System.out.println(table._headerNames.get(i));
		}
	}
}
