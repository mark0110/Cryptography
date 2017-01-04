import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class Decrypt {

	public static void main(String[] args) throws IOException {
		File file = new File(args[1]);
		PrintStream out = new PrintStream(new FileOutputStream(args[2]));
		System.setOut(out);
		Scanner scan = new Scanner(file);
		String key=args[0];
		StringBuffer line = new StringBuffer();
		while(scan.hasNextLine()){
			line.append(scan.nextLine());
			line=substitutionDecrypt(line, key);
			line=transpositionDecrypt(line, key);
			System.out.println(line);
			line.delete(0, line.length());
		}
	}
	
	public static StringBuffer substitutionPattern(String key){
		StringBuffer alphabet = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ ");
		StringBuffer alphabet1 = new StringBuffer(alphabet);
		int hash=key.hashCode();
		Random random = new Random(hash);
		int tempNum1=0, tempNum2=0;
		String swapStr="";
		for (int i=0; i<100; i++){
			tempNum1=random.nextInt(alphabet1.length());
			tempNum2=random.nextInt(alphabet1.length());
			swapStr=alphabet1.charAt(tempNum1)+"";
			alphabet1.replace(tempNum1, tempNum1+1, alphabet1.charAt(tempNum2)+"");
			alphabet1.replace(tempNum2, tempNum2+1, swapStr);
		}
		return alphabet1;
	}
	
	public static StringBuffer substitutionDecrypt(StringBuffer line, String key){
		StringBuffer alphabet = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ ");
		
		for (int i=0; i<line.length(); i++){
			for(int k=0; k<alphabet.length(); k++){
				if (Character.compare(line.charAt(i), substitutionPattern(key).charAt(k))==0){
					line.replace(i, i+1, alphabet.charAt(k)+"");
					break;
				}
			}
		}
		return line;
	}
	
	public static int[] pattern(String key){
		int[] pattern = new int[key.length()];
		for(int i=0; i<key.length(); i++){
			pattern[i]=i;
		}
		int hash=key.hashCode();
		Random random = new Random(hash);
		int swapInt=0;
		int tempNum1=0, tempNum2=0;
		for (int i=0; i<100; i++){
			tempNum1=random.nextInt(key.length());
			tempNum2=random.nextInt(key.length());
			swapInt=pattern[tempNum1];
			pattern[tempNum1]=pattern[tempNum2];
			pattern[tempNum2]=swapInt;
		}
		return pattern;
	}
	
	public static StringBuffer transpositionDecrypt(StringBuffer line, String key){
		char[][] trans = new char[key.length()][key.length()];
		int count=0;
		for(int i=0; i<key.length(); i++){
			for (int k=0; k<key.length(); k++){
				trans[k][pattern(key)[i]]=line.charAt(count);
				count++;
			}
		}
		String transLine="";
		for(int i=0; i<key.length(); i++){
			for (int k=0; k<key.length(); k++){
				transLine=transLine+trans[i][k];
			}
		}
		line.replace(0, line.length(), transLine);
		return line;
	}

}
