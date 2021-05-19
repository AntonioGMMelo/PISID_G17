<?php
	session_start();
?>

<!DOCTYPE html>
<html>
	<head>
		<title>Alertas</title>
		<meta charset="utf-8">

		<link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css" />
		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel="stylesheet" type="text/css" href="./css/verAlertasCultura.css">

	</head>
	<body >

		<h2>Ver Alertas</h2>

		<div id ="container2">

		<?php

			include_once '../Includes/DatabaseConn.php';

			$user_ID = $_GET['User_ID'];
			
			
			$dbConn = unserialize($_SESSION['dbConn']);

	
			$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
			

			//$sql = " SELECT * FROM alerta where Utilizador_ID = $user_ID ;";

			$sql = "CALL VerTodosAlertas('$user_ID');";
			$result = mysqli_query($conn, $sql);


			$resultCheck = mysqli_num_rows($result);

			$alertas = array( array());
			$numAlertas = 0;

			if($resultCheck > 0){
				while($row = mysqli_fetch_assoc($result)){
					$alertas[]=$row;
					
					$numAlertas++;
					
				}
			}else{
				echo "<h1>De momento não tem alertas</h1>";
			}

			
			echo "<div class='swiper-container'>";

  				echo"<div class='swiper-wrapper'>";

			for($i = $numAlertas; $i >= 1; $i--){
				echo "<div class='swiper-slide'>";
				echo "<div class='alerta'>";
				echo "<p>" . "ID do alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Alerta_ID'] . "</p>";
				echo "<p>" . "Hora do alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Hora'] . "</p>";
				echo "<p>" . "Medida recebida : ";
				echo "<p>" . $alertas[$i]['Leitura'] . "</p>";
				echo "<p>" . "Tipo do alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Tipo'] . "</p>";
				echo "<p>" . "Mensagem : " . "</p>";
				echo "<p>" . $alertas[$i]['Mensagem']  . "</p>";
				echo "<p>" . "Hora de escrita do alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['HoraEscrita'] . "</p>";
				echo "<p>" . "Zona do alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Zona_ID'] . "</p>";
				echo "<p>" . "Sensor que originou o alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Sensor_ID'] . "</p>";
				echo "<p>" . "Cultura associada ao alerta : " . "</p>";
				echo "<p>" . $alertas[$i]['Cultura_ID'] . "</p>";
				echo "<p>" . "ID do investigador : " . "</p>";
				echo "<p>" . $alertas[$i]['Utilizador_ID'] . "</p>";
				echo "<p>" . "Nome da cultura : " . "</p>";
				echo "<p>" . $alertas[$i]['Cultura'] . "</p>";
				echo "</div>";
			echo "</div>";

			}

			echo "</div>";

			echo"</div>"; 
					
		

		?>

			</div>

	<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>

	<script>
      var swiper = new Swiper(".swiper-container", {
        effect: "coverflow",
        grabCursor: true,
        centeredSlides: true,
        slidesPerView: "auto",
        coverflowEffect: {
			rotate: 30,
            stretch: 0,
            depth: 200,
            modifier: 1,
            slideShadows: true,
        },
      });
    </script>

	</body>
</html>