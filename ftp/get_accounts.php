<?php
	/*
	*	Obtiene los movimientos de una cuenta
	*/
	require 'Finanzas.php';

	$username = $_REQUEST['username'];

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{

		if(isset($username) && !empty($username))
		{
			//En caso de solo solicitar los movimientos de el usuario
			$body = json_decode(file_get_contents("php://input"), true);

			$query = Finanzas::getCuentas($username);

			if($query)
			{
				$data["state"] = 1;
				$data["items"] = $query;
				print json_encode($data);
			}
			else
			{
				$data["state"] = 0;
				$data["message"] = "Ha ocurrido un error";
				print json_encode($data);
			}
		}
	}
?>