<?php
	/*
	*	Obtiene los movimientos de una cuenta
	*/
	require 'Finanzas.php';

	$idUser = $_REQUEST['idUser'];
	$idMovement = $_REQUEST['minIdMovement'];

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		if(isset($idMovement) && !empty($idMovement))
		{
			$body = json_decode(file_get_contents("php://input"), true);

			$quantity = Finanzas::getQuantityOfMovements($idUser, $idMovement);
			$quantity = $quantity[0]['quantity'];

			if($quantity > 0)
			{
				$query = Finanzas::getMoreMovements($idUser, $idMovement);

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
			else
			{
				$data["state"] = 0;
				$data["message"] = "No hay mas movimientos";
				print json_encode($data);
			}
		}
		else if(isset($idUser) && !empty($idUser))
		{
			//En caso de solo solicitar los movimientos de el usuario
			$body = json_decode(file_get_contents("php://input"), true);

			$query = Finanzas::getMovements($idUser);

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
		else
		{
			$data["state"] = 0;
			$data["message"] = "No se recibieron datos";
			print json_encode($data);
		}
	}
?>