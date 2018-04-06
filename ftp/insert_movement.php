<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$idUser = $_REQUEST['idUsuario'];
		$idCategory = $_REQUEST['idCategoria'];
		$amount = $_REQUEST['monto'];
		$idAccount = $_REQUEST['idCuenta'];
		$type = $_REQUEST['tipo'];
		$date = $_REQUEST['fecha'];
		$concept = $_REQUEST['concepto'];
		$idAccountTransfer = $_REQUEST['idAccountTransfer'];
		$body = json_decode(file_get_contents("php://input"), true);

		$dinero = Finanzas::getDineroCuenta($idAccount);
		$dinero = $dinero[0]['dinero'];

		if(isset($idUser) && !empty($idUser) && 
			isset($idCategory) && !empty($idCategory) &&
			isset($amount) && !empty($amount) &&
			isset($idAccount) && !empty($idAccount) &&
			isset($type) && !empty($type) &&
			isset($date) && !empty($date) &&
			isset($concept) && !empty($concept))
		{
			if($type == 3)//Transferencia
			{
				if(isset($idAccountTransfer) && !empty($idAccountTransfer))
				{
					if(((float)$dinero - (float)$amount) >= 0)
					{
						Finanzas::restarDineroCuenta($idAccount, $amount);
						Finanzas::sumarDineroCuenta($idAccountTransfer, $amount);
						$registros = Finanzas::nuevoMovimientoTransfer($idUser, $idCategory, $amount, $idAccount, $type, $date, $concept, $idAccountTransfer);
						if($registros)
						{
							$datos["estado"] = 1;
							$datos["mensaje"] = "Movimiento Registrado";
							print json_encode($datos);
						}
						else
						{
							$datos["estado"] = 0;
							$datos["mensaje"] = "Ha ocurrido un error";
							print json_encode($datos);
						}
					}
					else
					{
						$datos["estado"] = 0;
						$datos["mensaje"] = "No cuenta con dinero suficiente para hacer esta transaccion";
						print json_encode($datos);
					}
				}
				else
				{
					$datos["estado"] = 0;
					$datos["mensaje"] = "Algunos datos no estan definidos";
					print json_encode($datos);
				}
			}
			else if($type == 2)//Egreso
			{
				if(((float)$dinero - (float)$amount) >= 0)
				{
					Finanzas::restarDineroCuenta($idAccount, $amount);
					$registros = Finanzas::nuevoMovimiento($idUser, $idCategory, $amount, $idAccount, $type, $date, $concept);
					if($registros)
					{
						$datos["estado"] = 1;
						$datos["mensaje"] = "Movimiento Registrado";
						print json_encode($datos);
					}
					else
					{
						$datos["estado"] = 0;
						$datos["mensaje"] = "Ha ocurrido un error";
						print json_encode($datos);
					}
				}
				else
				{
					$datos["estado"] = 0;
					$datos["mensaje"] = "No cuenta con dinero suficiente para hacer esta transaccion";
					print json_encode($datos);
				}
			}
			else //Ingreso
			{
				Finanzas::sumarDineroCuenta($idAccount, $amount);
				$registros = Finanzas::nuevoMovimiento($idUser, $idCategory, $amount, $idAccount, $type, $date, $concept);
				if($registros)
				{
					$datos["estado"] = 1;
					$datos["mensaje"] = "Movimiento Registrado";
					print json_encode($datos);
				}
				else
				{
					$datos["estado"] = 0;
					$datos["mensaje"] = "Ha ocurrido un error";
					print json_encode($datos);
				}
			}
		}
		else
		{
			$datos["estado"] = 0;
			$datos["mensaje"] = "Algunos datos no estan definidos";
			print json_encode($datos);
		}
	}
?>