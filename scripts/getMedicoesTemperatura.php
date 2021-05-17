	<?php
	$url="127.0.0.1";
	$database="estufadb";
    $conn = mysqli_connect($url,$_POST['username'],$_POST['password'],$database);
	$sql = "SELECT DISTINCT Medicao.Hora, Medicao.Leitura as Temperatura
            FROM medicao, utilizador, cultura
            where utilizador.EmailUtilizador = $_POST['username'] and utilizador.Utilizador_ID = cultura.Utilizador_ID and cultura.Zona_ID = medicao.Zona_ID and (Medicao.Sensor_ID = "T1" OR Medicao.Sensor_ID = "T2") AND Medicao.Valido = 1 AND Hora >= now() - interval 5 minute
            ORDER BY Hora ASC";
	$result = mysqli_query($conn, $sql);
	$response["medicoes"] = array();
	if ($result){
		if (mysqli_num_rows($result)>0){
			while($r=mysqli_fetch_assoc($result)){
				$ad = array();
				$ad["Hora"] = $r['Hora'];
				$ad["Leitura"] = $r['Leitura'];
				array_push($response["medicoes"], $ad);
			}
		}	
	}
	$json = json_encode($response["medicoes"]);
	echo $json;
	mysqli_close ($conn);