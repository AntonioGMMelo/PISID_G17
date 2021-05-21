	<?php
	$url="127.0.0.1";
	$database="estufadb";
    $conn = mysqli_connect($url,'root','',$database);
	$sql = "SELECT Alerta.Zona_ID, Alerta.Sensor_ID, Alerta.Hora, Alerta.Leitura, Alerta.Cultura_ID, Alerta.Cultura, Alerta.Utilizador_ID, Alerta.HoraEscrita, Alerta.Tipo, Alerta.Mensagem 
	 from Alerta, Utilizador where alerta.Utilizador_ID = Utilizador.Utilizador_ID AND Utilizador.EmailUtilizador = '".$_POST['username']."' AND DATE(Alerta.Hora) = '" . $_POST['date'] . "';";	
	$result = mysqli_query($conn, $sql);
	$response["avisos"] = array();
	if ($result){
		if (mysqli_num_rows($result)>0){
			while($r=mysqli_fetch_assoc($result)){
				$ad = array();
				$ad["Zona"] = $r['Zona_ID'];
				$ad["Sensor"] = $r['Sensor_ID'];
				$ad["Hora"] = $r['Hora'];
				$ad["Leitura"] = $r['Leitura'];
				$ad["IDCultura"] = $r['Cultura_ID'];
				$ad["Cultura"] = $r['Cultura'];
				$ad["IDUtilizador"] = $r['Utilizador_ID'];
				$ad["HoraEscrita"] = $r['HoraEscrita'];
				$ad["TipoAlerta"] = $r['Tipo'];
				$ad["Mensagem"] = $r['Mensagem'];
				array_push($response["avisos"], $ad);
			}
		}	
	}
	$json = json_encode($response["avisos"]);
	echo $json;
	mysqli_close ($conn);