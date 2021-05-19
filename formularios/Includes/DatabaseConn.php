// <?php

// $dbServerName = "localhost";
// $dbUserName = "root";
// $dbPassWord = "";
// $dbName = "estufadb";

// $conn = mysqli_connect($dbServerName, $dbUserName, $dbPassWord, $dbName );

class DatabaseConn {
    private $dbServerName = "localhost";
    private $dbUserName;
    private $dbPassWord;
    private $dbName = "estufadb";

    public function getDBServerName() {
    	return $this->dbServerName;
    }

    public function setDBServerName($serverName) {
    	$this->dbServerName = $serverName;
    }


    public function getDBUserName() {
    	return $this->dbUserName;
    }

    public function setDBUserName($userName) {
    	$this->dbUserName = $userName;
    }


    public function getDBPassword() {
    	return $this->dbPassWord;
    }

    public function setDBPassword($passWord) {
    	$this->dbPassWord = $passWord;
    }


    public function getDBName() {
    	return $this->dbName;
    }

    public function setDBName($name) {
    	$this->dbName = $name;
    }

}



?>