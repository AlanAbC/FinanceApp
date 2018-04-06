<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'GET')
	{
		$idUsuario = $_REQUEST['idU'];
		$nombre = $_REQUEST['name'];
		$descripcion = $_REQUEST['dsc'];
		$body = json_decode(file_get_contents("php://input"), true);
		$registros = Finanzas::getExistsCuenta($idUsuario, $nombre);

		if($registros[0]['total'] == 0)
		{
			$registros = Finanzas::nuevaCuenta($idUsuario, $nombre, $descripcion);
			if($registros)
			{
				$datos["estado"] = 1;
				$datos["mensaje"] = "Cuenta Registrada";
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
			$datos["mensaje"] = "La cuenta ya existe";
			print json_encode($datos);
		}
	}
?>