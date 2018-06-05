package main;

import java.util.function.DoubleFunction;

public class Matrix {
	
	float[][] matrix;
	int rows;
	int cols;
	/**
	 * Creates a new Matrix Object.
	 * @param rows amount of rows
	 * @param cols amount of columns
	 */
	public Matrix(int rows,int cols){
		this.matrix = new float[rows][cols];
		this.rows = rows;
		this.cols = cols;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				matrix[i][j] = 0;
			}
		}
	}
	/**
	 * Creates a Matrix object from saved String.
	 * @param data Stringdata
	 */
	public Matrix(String data){
		String[] sData = data.split(",matrix:\\{");
		rows = Integer.parseInt(sData[0].split(",")[0].split("rows: ")[1]);
		cols = Integer.parseInt(sData[0].split(",")[1].split("cols: ")[1]);
		matrix = new float[rows][cols];
		sData = sData[1].split("\\},\\{");
		for(int i = 0; i < rows; i++){
			String[] tmpData = sData[i].split("\\{");
			tmpData = tmpData[tmpData.length-1].split("\\}")[0].split(",");
			for(int j = 0; j < cols; j++){
				matrix[i][j] = Float.parseFloat(tmpData[j]);
			}
		}
	}
	
	 /**
	  * Fills the Matrix with random values.
	  */
	public void randomize(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				matrix[i][j] = (float) (Math.random()*2-1);
			}
		}
	}

	/**
	 * Multiplies every Element of this Matrix Object with s.
	 * @param s Scalar
	 */
	public void multiply(float s){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				matrix[i][j] *= s;
			}
		}
	}
	
	/**
	 * Multiplies every Element of given Matrixobject with s.
	 * @param matrix given Matrix
	 * @param s Scalar
	 */
	public static Matrix multiply(Matrix matrix, float s){
		Matrix retVal = new Matrix(matrix.rows, matrix.cols);
		for(int i = 0; i < matrix.rows; i++){
			for(int j = 0; j < matrix.cols; j++){
				retVal.matrix[i][j] = matrix.matrix[i][j] * s;
			}
		}
		return retVal;
	}
	
	/**
	 * Multiply every Element of this Matrix with every corresponding Element of a given Matrix.
	 * Columns and Rows of A must match Columns and Rows of B.
	 * @param m given Matrix
	 */
	public void multiply(Matrix m){
		if(this.cols != m.cols || this.rows != m.rows){
			System.out.println(this);
			System.out.println(m);
			System.out.println("Columns and Rows of A must match Columns and Rows of B.");
			return;
		}
		for(int i = 0; i < this.rows; i++){
			for(int j = 0; j < m.cols; j++){
					this.matrix[i][j] *= m.matrix[i][j];
			}
		}
	}
	
	/**
	 * Dotproduct of Matrix A and Matrix B. Columns of A and rows of B have to be the same size!
	 * @param m given Matrix
	 */
	public static Matrix multiply(Matrix matrix, Matrix m){
		if(matrix.cols != m.rows){
			System.out.println(matrix);
			System.out.println(m);
			System.out.println("Columns of A and rows of B have to be the same size!");
			return null;
		}
		Matrix retVal = new Matrix(matrix.rows, m.cols);
		for(int i = 0; i < retVal.rows; i++){
			for(int j = 0; j < retVal.cols; j++){
				float sum = 0f;
				for(int k = 0; k < matrix.cols; k++){
					sum += matrix.matrix[i][k] * m.matrix[k][j];
				}
				retVal.matrix[i][j] = sum;
			}
		}
		
		return retVal;
	}
	
	/**
	 * Adds s to every Element of this Matrix.
	 * @param s 
	 */
	public void add(int s){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				matrix[i][j] += s;
			}
		}
	}
	
	/**
	 * Add every Element of this Matrix with every corresponding Element of a given Matrix.
	 * Columns and Rows of A must match Columns and Rows of B.
	 * @param m given Matrix
	 */
	public void add(Matrix m){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				matrix[i][j] += m.matrix[i][j];
			}
		}
	}
	
	/**
	 * Adds s to every Element of the given Matrix.
	 * @param s 
	 */
	public static Matrix add(Matrix matrix, int s){
		Matrix retVal = new Matrix(matrix.rows, matrix.cols);
		for(int i = 0; i < matrix.rows; i++){
			for(int j = 0; j < matrix.cols; j++){
				retVal.matrix[i][j] = matrix.matrix[i][j] + s;
			}
		}
		return retVal;
	}
	
	/**
	 * Add every Element of a given Matrix with every corresponding Element of another given Matrix.
	 * Columns and Rows of A must match Columns and Rows of B.
	 * @param m given Matrix
	 */
	public static Matrix add(Matrix matrix, Matrix m){
		Matrix retVal = new Matrix(matrix.rows, matrix.cols);
		for(int i = 0; i < matrix.rows; i++){
			for(int j = 0; j < matrix.cols; j++){
				retVal.matrix[i][j] = matrix.matrix[i][j] + m.matrix[i][j];
			}
		}
		return retVal;
	}
	
	/**
	 * Subtract every Element of a given Matrix with every corresponding Element of another given Matrix.
	 * Columns and Rows of A must match Columns and Rows of B.
	 * @param m given Matrix
	 */
	public static Matrix subtract(Matrix a, Matrix b){
		Matrix retVal = new Matrix(a.rows, a.cols);
		for(int i = 0; i < a.rows; i++){
			for(int j = 0; j < a.cols; j++){
				retVal.matrix[i][j] = a.matrix[i][j] - b.matrix[i][j];
			}
		}
		return retVal;
	}
	
	/**
	 * Transpose given Matrix.
	 * @param m matrix to transpose
	 * @return transposed Matrix
	 */
	public static Matrix transpose(Matrix m){
		Matrix retVal = new Matrix(m.cols, m.rows);
		
		for(int i = 0; i < retVal.rows; i++){
			for(int j = 0; j < retVal.cols; j++){
				retVal.matrix[i][j] = m.matrix[j][i];
			}
		}
		
		return retVal;
	}

	/**
	 * Create Matrix from float Array.
	 * @param array 
	 * @return new Matrix
	 */
	public static Matrix fromArray(float[] array){
		Matrix inputM = new Matrix(array.length,1);
		for(int i = 0; i < array.length; i++){
			inputM.matrix[i][0] = array[i];
		}
		return inputM;
	}

	
	/**
	 * Create float Array from Matrix.
	 * @return Array with Type float
	 */
	public float[] toArray() {
		float[] retVal = new float[matrix.length * matrix[0].length];
		for(int i = 0; i < retVal.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				retVal[i * matrix[i].length + j] = matrix[i][j];
			}
		}
		return retVal;
	}
	
	/**
	 * Apply given function to everyElement of this Matrix.
	 * @param func
	 */
	public void map(DoubleFunction<Float> func){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = func.apply(matrix[i][j]);
			}
		}
	}
	
	/**
	 * Apply given function to everyElement of a given Matrix.
	 * @param func
	 */
	public static Matrix map(Matrix m, DoubleFunction<Float> func){
		Matrix retVal = new Matrix(m.rows, m.cols);
		for(int i = 0; i < retVal.matrix.length; i++){
			for(int j = 0; j < retVal.matrix[i].length; j++){
				retVal.matrix[i][j] = func.apply(m.matrix[i][j]);
			}
		}
		return retVal;
	}

	public String toString(){
		String s = "";
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				s += matrix[i][j] + " ";
			}
			s+="\n";
		}
		return s;
	}
	
	/**
	 * Create Datastring to save this Matrix into a File. A String created by this Method can create a Matrix with new Matrix(string)
	 * @return datastring of this Matrix
	 */
	public String getData() {
		String retVal = "rows: " + rows + ", cols: " + cols;
		retVal += ",matrix:{";
		
		for(int i = 0; i < rows;i++){
			retVal += "{";
			for(int j = 0; j < cols; j++){
				retVal += matrix[i][j];
				if(j < cols-1){
					retVal += ",";
				}
			}
			if(i < rows-1){
				retVal += "},";
			}else{
				retVal += "}";
			}
		}
		
		retVal += "}";
		return retVal;
	}
}
