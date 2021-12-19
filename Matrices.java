//Andre Saldanha - CS131 Semester Project
//Using Matrices to Manipulate Images and Encrypt Messages

import java.awt.BorderLayout;
import java.awt.image.*;
import java.awt.Desktop;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Matrices {	

	public static void main(String[] args) throws IOException {

		Scanner keyboard = new Scanner(System.in);
		

		System.out.println("\n***PART 1: Encryption using Matrices***");
		
		String[] alphabet = {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		
		//These Matrices are inverses of each other
		int[][] encryptionMatrix = { {1,-2, 2}, 
									 {-1, 1, 3},
									 {1, -1, -4} };
		int[][] decryptionMatrix = { {-1,-10, -8}, 
									 {-1, -6, -5},
									 {0, -1, -1} };
		int n = encryptionMatrix.length;
		
		keyboard.nextLine();
		int[][] a = matrixMultiplication(encryptionMatrix, decryptionMatrix);
		System.out.println("Matrix Multiplication: Encryption and Decryption Matirx");
		for(int i = 0; i < n;i++) {
			for(int j = 0; j < n; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
		
		keyboard.nextLine();
		System.out.println("\n\nEnter message to be encrypted");
		String msg = keyboard.nextLine();
		msg = msg.toUpperCase();

		//change the letters into numbers
		int length = msg.length() + 3 - msg.length()%n;
		int[] numbers = new int[length];
		int[] secretNumbers = new int[length];
		int[] decryptedNumbers = new int[length];
		for(int i = 0; i < msg.length(); i++) {
			for(int index = 0; index < alphabet.length; index++) {
				if(msg.substring(i, i + 1).equals(alphabet[index])) {
					numbers[i] = index;
					System.out.print(index + " ");
					break;
				}
			}
		}
		for(int i = msg.length(); i < length; i++) {
			numbers[i] = 0;
			System.out.print("0 ");
		}

		//Encrypt the Numbers
		keyboard.nextLine();
		System.out.print("\nEncrypted Numbers");
		for(int i = 0; i < length/n; i++) {
			keyboard.nextLine();
			int[][] array = new int[1][n];
			for(int j = 0; j < n; j++) {
				array[0][j] = numbers[3*i + j];
				System.out.print(numbers[3*i + j] + " ");
			}

			System.out.print(" ----> ");
			array = matrixMultiplication(array, encryptionMatrix);

			for(int j = 0; j < n; j++) {
				secretNumbers[3*i + j] = array[0][j];
				System.out.print(secretNumbers[3*i + j] + " ");
			}
		}
		
		System.out.println("\n");
		for(int i = 0; i < length; i++) {
			System.out.print(secretNumbers[i] + " ");
		}
		System.out.println();
		
		//Decrypt the Numbers
		keyboard.nextLine();
		System.out.print("\nDecrypted Numbers");
		for(int i = 0; i < length/n; i++) {
			keyboard.nextLine();
			int[][] array = new int[1][n];
			for(int j = 0; j < n; j++) {
				array[0][j] = secretNumbers[3*i + j];
				System.out.print(secretNumbers[3*i + j] + " ");
			}
			
			System.out.print(" ----> ");
			array = matrixMultiplication(array, decryptionMatrix);
		
			for(int j = 0; j < n; j++) {
				decryptedNumbers[3*i + j] = array[0][j];
				System.out.print(decryptedNumbers[3*i + j] + " ");
			}
		}
		
		System.out.println("\n\n\nDecrypted Message:");
		for(int i = 0; i < length; i++) {
			System.out.print(alphabet[decryptedNumbers[i]]);
		}

		
		//********************************************************************************************
		
		keyboard.nextLine();
		System.out.println("\n\n\n\n\n***PART 2: Manipulating images using Matrices***\n");
		
		//Can load any image stored on your drive
		BufferedImage img = ImageIO.read(new File("dog.jpg"));
		int matrix[][] = getMatrixOfImage(img);

		int choice = 0;

		while(choice != 6) {
			System.out.println("How would you like to manipulate the image?");
			System.out.println("1. Rotate Image (Transpose)");
			System.out.println("2. Invert Color of Image (Multiply)");
			System.out.println("3. Reflect Image");
			System.out.println("4. Stretch Image");
			System.out.println("5. View Image");
			System.out.println("6. Quit");
			choice = keyboard.nextInt();

			if(choice == 1) {
				matrix = transposeMatrix(matrix);
				MatrixToImage(matrix);
			}
			else if(choice == 2) {
				matrix = scalarMultiplication(matrix, -1);
				MatrixToImage(matrix);
			}
			else if(choice == 3) {
				matrix = reflectMatrix(matrix);
				MatrixToImage(matrix);
			}
			else if(choice == 4) {
				matrix = stretchMatrix(matrix);
				MatrixToImage(matrix);
			}
			else if(choice == 5) {
				MatrixToImage(matrix);
			}
		}

	}
	
	//The following methods are operations that can be performed on any matrix
	
	public static int[][] matrixMultiplication(int[][] matrixA, int[][] matrixB) {
		int k = matrixB.length;

		if(matrixB.length != matrixA[0].length) {
			System.out.println("INVALID MULTIPLICATION. MATRICES ARE NOT APPROPRIATE SIZE");
			return null;
		}

		int m = matrixA.length;
		int n = matrixB[0].length;
		int[][] matrix = new int[m][n];

		for (int i = 0; i < m; i++) {

			int[] row = matrixA[i];
			for (int j = 0; j < n; j++) {
				int sum = 0;
				for(int index = 0; index < k; index++) {
					sum += row[index] * matrixB[index][j];
				}
				matrix[i][j] = sum;
			}

		}
		return matrix;
	}

	public static int[][] transposeMatrix(int[][] matrix) {
		int m = matrix[0].length;
		int n = matrix.length;
		int transpose[][] = new int[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				transpose[i][j] = matrix[j][i];
			}
		}
		return transpose;
	}

	public static int[][] scalarMultiplication(int[][] matrix, int scalar) {		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = scalar * matrix[i][j];
			}
		}

		return matrix;
	}

	public static int[][] reflectMatrix(int[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] newMatrix = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				newMatrix[i][j] = matrix[m - i - 1][j];
			}
		}
		return newMatrix;
	}

	public static int[][] stretchMatrix(int[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] newMatrix = new int[2*m][n];
		for (int i = 0; i < m; i++) {
			newMatrix[2*i] = matrix[i];
			newMatrix[2*i + 1] = matrix[i];
		}
		return newMatrix;
	}
	
	//The following two methods are use to convert an image to a matrix, and vice versa.

	public static int[][] getMatrixOfImage(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth(null);
		int height = bufferedImage.getHeight(null);

		int matrix[][]= new int[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				matrix[i][j] = bufferedImage.getRGB(i, j);
			}
		}
		return matrix;
	}

	public static void MatrixToImage(int[][] matrix) {
		int width = matrix.length;
		int height = matrix[0].length;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix[x][y]);
			}
		}

		try {
			ImageIO.write(image, "jpg", new File("edit.jpg"));	
			File f = new File("edit.jpg");
			Desktop dt = Desktop.getDesktop();
			dt.open(f);
		} catch (IOException e) {}

	}

}