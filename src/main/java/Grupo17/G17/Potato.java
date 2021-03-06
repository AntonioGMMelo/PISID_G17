package Grupo17.G17;

import java.util.Deque;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public class Potato {

	private static Deque<Double> isValidT1 = new LinkedList<Double>();
	private static Deque<Double> medicoesT1 = new LinkedList<Double>();
	private static Deque<Double> isValidT2 = new LinkedList<Double>();
	private static Deque<Double> medicoesT2 = new LinkedList<Double>();
	private static Deque<Double> isValidH1 = new LinkedList<Double>();
	private static Deque<Double> medicoesH1 = new LinkedList<Double>();
	private static Deque<Double> isValidH2 = new LinkedList<Double>();
	private static Deque<Double> medicoesH2 = new LinkedList<Double>();
	private static Deque<Double> medicoesL1 = new LinkedList<Double>();
	private static Deque<Double> medicoesL2 = new LinkedList<Double>();
	private static Dictionary<Integer, Integer> lastAlert = new Hashtable();
	
	public static Dictionary<Integer, Integer> getLastAlert() {
		return lastAlert;
	}
	public static Deque<Double> getIsValidT1() {
		return isValidT1;
	}
	public void setIsValidT1(Deque<Double> isValidT1) {
		this.isValidT1 = isValidT1;
	}
	public static Deque<Double> getMedicoesT1() {
		return medicoesT1;
	}
	public static Deque<Double> getIsValidT2() {
		return isValidT2;
	}
	public static Deque<Double> getMedicoesT2() {
		return medicoesT2;
	}
	public static Deque<Double> getIsValidH1() {
		return isValidH1;
	}
	public static Deque<Double> getMedicoesH1() {
		return medicoesH1;
	}
	public static Deque<Double> getIsValidH2() {
		return isValidH2;
	}
	public static Deque<Double> getMedicoesH2() {
		return medicoesH2;
	}
	public static Deque<Double> getMedicoesL1() {
		return medicoesL1;
	}
	public static Deque<Double> getMedicoesL2() {
		return medicoesL2;
	}
	
}
