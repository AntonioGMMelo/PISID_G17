	<?php
	$url="127.0.0.1";
	$database="estufadb";
    $conn = mysqli_connect($url,$_POST['username'],$_POST['password'],$database);
	$sql = "SELECT * from Alerta where DATE(Alerta.Hora) = '" . $_POST['date'] . "';";	
	$result = mysqli_query($conn, $sql);
	$response["avisos"] = array();
	if ($result){
		if (mysqli_num_rows($result)>0){
			while($r=mysqli_fetch_assoc($result)){
				$ad = array();
				$ad["Alerta"] = $r['Alerta_ID'];
				$ad["Zona"] = $r['Zona_ID'];
				$ad["Sensor"] = $r['Sensor_ID'];
				$ad["Hora"] = $r['Hora'];
				$ad["Leitura"] = $r['Leitura'];
				$ad["Cultura_ID"] = $r['Cultura_ID'];
				$ad["Cultura"] = $r['Cultura'];
				$ad["Utilizador"] = $r['Utilizador_ID'];
				$ad["HoraEscrita"] = $r['HoraEscrita'];
				$ad["Tipo"] = $r['Tipo'];
				$ad["Mensagem"] = $r['Mensagem'];
				...
				array_push($response["avisos"], $ad);
			}
		}	
	}
	$json = json_encode($response["avisos"]);
	echo $json;
	mysqli_close ($conn);