<?php
	session_start();
	include_once '../Includes/DatabaseConn.php';
?>
<!DOCTYPE html>
<html>
	<head>
		<title>associar Cultura</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel = "stylesheet" type="text/css" href="../Investigador/css/alterarParametrosCultura.css">
		<link rel="stylesheet" type="text/css" href="./css/criarUtilizador.css">	</head>
		</head>
	<body >

		<h2>Associar Cultura a Investigador</h2>
		
		<div id="container">

			

			<form action="../Includes/culturaAssocInvestigador_inc.php" method="POST">

				<fieldset>

					<legend>associar cultura a Investigador</legend>

				
			

					<div class="parametros">
					<div>
						<label for="nome">Nome da Cultura </label>
						<select name="nome" id="nome">

							<?php
								$dbConn = unserialize($_SESSION['dbConn']);
								$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
								if (!$conn) {
							  		die("Connection failed: " . mysqli_connect_error());
								}

								$sql = "SELECT * FROM cultura ;";
								$result = mysqli_query($conn, $sql);
								$numCulturas = mysqli_num_rows($result);

								$culturas = array(array());
								

								if($numCulturas > 0){
									while($row = mysqli_fetch_assoc($result)){
										$culturas[]=$row;
									
										
									}
								}

								for($i = 1; $i <= $numCulturas; $i++){
									$nomeCultura = $culturas[$i]['NomeCultura'];
									echo "<option> $nomeCultura </option>";

								}



							?>

						    
					    
						</select>
					
					</div>

					<div>
						<label for="email">Email Investigador </label>
						<select name="email" id="email">

							<?php

								$sql = "SELECT * FROM utilizador where TipoUtilizador = 'Investigador' ;";
								$result = mysqli_query($conn, $sql);
								$numCulturas = mysqli_num_rows($result);

								$utilizador = array(array());
								

								if($numCulturas > 0){
									while($row = mysqli_fetch_assoc($result)){
										$utilizador[]=$row;
									
										
									}
								}

								for($i = 1; $i <= $numCulturas; $i++){
									$emailUtilizador = $utilizador[$i]['EmailUtilizador'];
									echo "<option> $emailUtilizador </option>";

								}

							?>

						</select>

					</div>



					
					</div>
 

				</fieldset>

				<?php

				if(isset($_GET['assoc']) && $_GET['assoc'] == 'insucess'){ ?>

				
					<div id="error" class="text-danger">
						Associação não permitida
					</div>
				

				<?php } ?>



				<div>
					<button  type="submit" name="submit" >Associar</button>
				</div>
			</form>

		</div>


				<?php

				if(isset($_GET['assoc']) && $_GET['assoc'] == 'sucess'){ ?>

				
					<div id="succes" class="text-danger">
						associação executada com exito!
					</div>
				

				<?php } ?>


	</body>
</html>