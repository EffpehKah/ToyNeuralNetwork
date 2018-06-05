package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.DoubleFunction;

public class NeuralNetwork {
	int inputs;
	int hidden;
	int output;
	
	float learning_rate;
	
	Matrix weights_ih;
	Matrix weights_ho;
	Matrix bias_h;
	Matrix bias_o;
	
	boolean debug = false;

	/**
	 * Creates a new Neural Network with given parameters.
	 * @param inputs amount of inputnodes
	 * @param hidden amount of hiddennodes
	 * @param output amount of outputnodes
	 */
	public NeuralNetwork(int inputs, int hidden, int output){
		this.inputs = inputs;
		this.hidden = hidden;
		this.output = output;
		this.learning_rate = 0.1f;

		weights_ih = new Matrix(hidden,inputs);
		weights_ih.randomize();
		
		weights_ho = new Matrix(output, hidden);
		weights_ho.randomize();
		
		bias_h = new Matrix(hidden, 1);
		bias_o = new Matrix(output,1);
		
		bias_h.randomize();
		bias_o.randomize();
	}

	/**
	 * Creates a Neural Network with given datastring
	 * @param data datastring created by getData();
	 */
	public NeuralNetwork(String data){
		String[] sData = data.split("\\}\\}");
		weights_ih = new Matrix(sData[0].split("weights_ih:\\{")[1]);
		weights_ho = new Matrix(sData[1].split("weights_ho:\\{")[1]);
		bias_h = new Matrix(sData[2].split("bias_h:\\{")[1]);
		bias_o = new Matrix(sData[3].split("bias_o:\\{")[1]);
		sData = sData[0].split("weights_ih:")[0].split(",");
		
		inputs = Integer.parseInt(sData[0]);
		hidden = Integer.parseInt(sData[1]);
		output = Integer.parseInt(sData[2]);
		learning_rate = Float.parseFloat(sData[3]);
	}
	
	/**
	 * Let the Neural Network guess.
	 * @param input
	 * @return
	 */
	public Matrix feedForward(Matrix input){
		//Generating Hidden Outputs!
		Matrix hidden = Matrix.multiply(weights_ih, input);
		hidden.add(bias_h);
		//activation function!
		hidden.map(NeuralNetwork::sigmoid);
		
		//Generating the output!
		Matrix output = Matrix.multiply(weights_ho,hidden);
		output.add(bias_o);
		output.map(NeuralNetwork::sigmoid);
		
		return output;
	}
	
	/**
	 * Let the Neural Network guess.
	 * @param input
	 * @return
	 */
	public float[] feedForward(float[] input){
		return feedForward(Matrix.fromArray(input)).toArray();
	}
	
	/**
	 * Train your Network to be the next best thing!
	 * @param input_array
	 * @param target_array
	 */
	public void train(float[] input_array, float[] target_array){
		Matrix inputs = Matrix.fromArray(input_array);
		Matrix targets = Matrix.fromArray(target_array);
		
		//Generating Hidden Outputs!
		Matrix hidden = Matrix.multiply(weights_ih, inputs);
		hidden.add(bias_h);
		//activation function!
		hidden.map(NeuralNetwork::sigmoid);
		
		//Generating the output!
		Matrix outputs = Matrix.multiply(weights_ho,hidden);
		outputs.add(bias_o);
		outputs.map(NeuralNetwork::sigmoid);
		

		//calculate the error
		Matrix output_errors = Matrix.subtract(targets, outputs);
		
		//calculate gradients
		Matrix gradients = Matrix.map(outputs,NeuralNetwork::dsigmoid);
		gradients.multiply(output_errors);
		gradients.multiply(learning_rate);
		
		//calculate hidden->output deltas
		Matrix hidden_T = Matrix.transpose(hidden);
		Matrix weight_ho_deltas = Matrix.multiply(gradients, hidden_T);
		
		weights_ho.add(weight_ho_deltas);
		bias_o.add(gradients);
		
		//calculate the hidden layer error
		Matrix who_t = Matrix.transpose(weights_ho);
		Matrix hidden_errors = Matrix.multiply(who_t, output_errors);
		
		//calculate hidden gradient
		Matrix hidden_gradient = Matrix.map(hidden, NeuralNetwork::dsigmoid);
		hidden_gradient.multiply(hidden_errors); 
		hidden_gradient.multiply(learning_rate);
		
		//calculate input->hidden deltas
		Matrix inputs_T = Matrix.transpose(inputs);
		Matrix weight_ih_deltas = Matrix.multiply(hidden_gradient, inputs_T);
		
		weights_ih.add(weight_ih_deltas);
		bias_h.add(hidden_gradient);
	}
	
	public void mutate(DoubleFunction<Float> func){
		this.weights_ih.map(func);
	    this.weights_ho.map(func);
	    this.bias_h.map(func);
	    this.bias_o.map(func);
	}
	
	public NeuralNetwork copy(){
		return new NeuralNetwork(this.getData());
	}
	
	/**
	 * Activation function for this Neural Network.
	 * @param n
	 * @return
	 */
	public static float sigmoid(double n){
		return (float)(1 / (1 + Math.exp(-n)));
	}
	
	/**
	 * De-Activation function for this Neural Network.
	 * @param n
	 * @return
	 */
	public static float dsigmoid(double n){
		return (float) (n * (1 - n));
	}
	
	/**
	 * Creates Datastring do save your Neural Network. You can create the old Neural Network with new NeuralNetwork(datastring)
	 * @return
	 */
	public String getData() {
		String retVal = inputs + "," + hidden + "," + output + "," + learning_rate;
		retVal += ",weights_ih:{" + weights_ih.getData() + "}";
		retVal += ",weights_ho:{" + weights_ho.getData() + "}";
		retVal += ",bias_h:{" + bias_h.getData() + "}";
		retVal += ",bias_o:{" + bias_o.getData() + "}";
		return retVal;
	}
	
	/**
	 * Save your NeuralNetwork.
	 * @param name
	 */
	public void saveData(String name){
		try {
			FileWriter write = new FileWriter(name);
			write.write(getData());
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Load your NeuralNetwork.
	 * @param name
	 * @return
	 */
	public static String loadData(String name){
		String retVal = "";
		try {
			BufferedReader read = new BufferedReader(new FileReader(name));
			retVal += read.readLine();
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
}
