<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{
		$idMovement = $_REQUEST['idMovement'];
		$idCategory = $_REQUEST['idCategoria'];
		$amount = $_REQUEST['monto'];
		$idAccount = $_REQUEST['idCuenta'];
		$type = $_REQUEST['tipo'];
		$concept = $_REQUEST['concepto'];
		$idAccountTransfer = $_REQUEST['idAccountTransfer'];
		$body = json_decode(file_get_contents("php://input"), true);

		$dinero = Finanzas::getDineroCuenta($idAccount);
		$dinero = (float)$dinero[0]['dinero'];

		if(isset($idCategory) && !empty($idCategory) &&
			isset($amount) && !empty($amount) &&
			isset($idAccount) && !empty($idAccount) &&
			isset($type) && !empty($type) &&
			isset($concept) && !empty($concept))
		{
			//Deshacemos los movimientos de las centas
			$movement = Finanzas::getMovement($idMovement);
			$moneyLastMovement = 0;
			if($movement[0]['Tipo'] == 1)//Ingreso
			{
				$moneyLastMovement = (-1) * (float)$movement[0]['Monto'];
			}
			else
			{
				$moneyLastMovement = (float)$movement[0]['Monto'];
			}
			
			if($type == 3)//Transferencia
			{
				if(isset($idAccountTransfer) && !empty($idAccountTransfer))
				{
					if((($dinero + $moneyLastMovement) - (float)$amount) >= 0)
					{
						Finanzas::restarDineroCuenta($idAccount, $amount);
						Finanzas::sumarDineroCuenta($idAccountTransfer, $amount);
						$registros = Finanzas::modifyMovementTransfer($idMovement, $idCategory, $amount, $idAccount, $type, $concept, $idAccountTransfer);
						if($registros)
						{
							$datos["estado"] = 1;
							$datos["mensaje"] = "Movimiento Actualizado";
							print json_encode($datos);

							if($movement[0]['Tipo'] == 1)//Ingreso
							{
								Finanzas::restarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
							}
							else if($movement[0]['Tipo'] == 2)//Egreso
							{
								Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
							}
							else //Transferencia
							{
								Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
								Finanzas::restarDineroCuenta($movement[0]['idCuentaTransfer'], $movement[0]['Monto']);

							}
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
				if((($dinero + $moneyLastMovement) - (float)$amount) >= 0)
				{
					Finanzas::restarDineroCuenta($idAccount, $amount);
					$registros = Finanzas::modifyMovement($idMovement, $idCategory, $amount, $idAccount, $type, $concept);
					if($registros)
					{
						$datos["estado"] = 1;
						$datos["mensaje"] = "Movimiento Actualizado";
						print json_encode($datos);

						if($movement[0]['Tipo'] == 1)//Ingreso
						{
							Finanzas::restarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
						}
						else if($movement[0]['Tipo'] == 2)//Egreso
						{
							Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
						}
						else //Transferencia
						{
							Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
							Finanzas::restarDineroCuenta($movement[0]['idCuentaTransfer'], $movement[0]['Monto']);

						}
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
					$datos["mensaje"] = "No cuenta con dinero suficiente para hacer este movimiento";
					print json_encode($datos);
				}
			}
			else //Ingreso
			{
				Finanzas::sumarDineroCuenta($idAccount, $amount);
				$registros = Finanzas::modifyMovement($idMovement, $idCategory, $amount, $idAccount, $type, $concept);
				if($registros)
				{
					$datos["estado"] = 1;
					$datos["mensaje"] = "Movimiento Actualizado";
					print json_encode($datos);

					if($movement[0]['Tipo'] == 1)//Ingreso
					{
						Finanzas::restarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
					}
					else if($movement[0]['Tipo'] == 2)//Egreso
					{
						Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
					}
					else //Transferencia
					{
						Finanzas::sumarDineroCuenta($movement[0]['idCuenta'], $movement[0]['Monto']);
						Finanzas::restarDineroCuenta($movement[0]['idCuentaTransfer'], $movement[0]['Monto']);

					}
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