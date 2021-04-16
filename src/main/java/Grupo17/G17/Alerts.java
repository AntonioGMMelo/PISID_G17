package Grupo17.G17;

//Class To Create The Alert Function
public class Alerts {

	public Alerts() {
		
	}
	
	public static String[] Alertar(double medicao, double[] ultimasMedicoes, double[] parametrosCultura, boolean tipoDeVerificacao) {

		if(tipoDeVerificacao) { //Valid Measurements
			
			if(medicao >= parametrosCultura[1] || medicao <= parametrosCultura[0]) return new String[] {"R", "FORA DOS LIMITES!"};			

			if(medicao >= 0.85*(parametrosCultura[1] - parametrosCultura[0]) + parametrosCultura[0] || medicao <= 0.15*(parametrosCultura[1] - parametrosCultura[0]) + parametrosCultura[0]) return new String[] {"O", "Proximo ao Limete!"};
			
			double sum = 0;
			
			for(int i = 10 ; i < ultimasMedicoes.length ; i++) sum += Math.sqrt(Math.pow(ultimasMedicoes[i] - medicao, 2));
		
			if(sum >= 1) return new String[] {"G", "Sistema Evoluindo Rapidamente Para Baixo/Cima"};
			
		}else{// Invalid Measurements
			
			int count = 0;
			
			for(int i = 0 ; i < ultimasMedicoes.length ; i++) if(ultimasMedicoes[i] == 0) count++;
			
			if(count == 20) return new String[] {"B", "SENSOR QUEBRADO!"};
			
			if(count >= 10) return  new String[] {"Y", "Sensor possivelmente danificado"};
			
		}
	
		return null;
		
	}
	
	public static void main(String[] args) {
		
		// null
		System.out.println(Alertar(20.0, new double[] {20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0}, new double[] {10.0, 40.0}, true));
	
		//RED
		System.out.println(Alertar(50.0, new double[] {20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//RED
		System.out.println(Alertar(5.0, new double[] {20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//Orange
		System.out.println(Alertar(35.6, new double[] {20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//Orange
		System.out.println(Alertar(14.4, new double[] {20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//Green
		System.out.println(Alertar(20.0, new double[] {20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0, 28.0, 29.0, 28.0, 27.0, 26.0, 25.0, 24.0, 23.0, 22.0, 21.0, 20.0, 21.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//Green
		System.out.println(Alertar(20.0, new double[] {19.0, 18.0, 17.0, 16.0, 15.0, 16.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0, 15.0}, new double[] {10.0, 40.0}, true)[1]);
		
		//Yellow
		System.out.println(Alertar(20.0, new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new double[] {10.0, 40.0}, false)[1]);
		
		//Black
		System.out.println(Alertar(20.0, new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[] {10.0, 40.0}, false)[1]);
		
	}
	
}
