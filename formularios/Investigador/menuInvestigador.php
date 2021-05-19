<?php
	session_start();
?>

<!DOCTYPE html>
<html>
	<head>
		<title>Menu Investigador</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel="stylesheet" type="text/css" href="./css/menuInvestigador.css">
	</head>
	<body >
		
		<h2>MENU INVESTIGADOR</h2>

		<div id = "container">
			<?php

			print_r($_GET['ID']);

			include_once '../Includes/DatabaseConn.php';
			
			$dbConn = unserialize($_SESSION['dbConn']);

	
			$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
	
			
			$ID = $_GET['ID'];
		
		
			$sql = " SELECT * FROM cultura where Utilizador_ID = $ID ;";

			$result = mysqli_query($conn, $sql);


			$resultCheck = mysqli_num_rows($result);

			$culturas = array( array());
			$numCulturas = 0;

			if($resultCheck > 0){
				while($row = mysqli_fetch_assoc($result)){
					$culturas[]=$row;
					$numCulturas++;
				
				}
			}
		
		
			?>

			<div id="culturas">

			<h3>As suas culturas:</h3>

			<?php

				if($numCulturas == 0){?>
					Não tem culturas associadas
			<?php

				}else{
					for($i = 1; $i <= $numCulturas; $i++){
						echo "<div class = 'cultura'>";
						$cultura_ID = $culturas[ $i ]['Cultura_ID'] ; 
						$link = "dadosCultura.php?Cultura_ID="  . "$cultura_ID";
						$nomeCultura = $culturas[ $i ]['NomeCultura'];
						echo "<a href = '$link'>$nomeCultura</a>";
						echo "</div>";
						?>
			<?php
					
					

					}
				
				}
			?>
			</div>
				<hr>

			<div id="alerts">
				<h3>Alertas:</h3>
				<?php

					$link = "verAlertas.php?User_ID="  . "$ID";
					echo "<a id='Alertas' href = '$link'>Ver alertas</a>";
				?>
			</div>
	</div>

	</body>
</html>