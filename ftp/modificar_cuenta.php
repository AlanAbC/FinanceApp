<?php
	require 'Finanzas.php';

	if($_SERVER['REQUEST_METHOD'] == 'POST')
	{

		$id          = $_REQUEST['id']; // id de la cuenta, no del usuario
		$nombre      = $_REQUEST['name'];
		$descripcion = $_REQUEST['description'];
		$body        = json_decode(file_get_contents("php://input"), true);

		$registros = Finanzas::modificarCuenta($id, $nombre, $descripcion);

		if($registros)
		{
			$datos["estado"]  = 1;
			$datos["mensaje"] = "Categoria Modificada";
			print json_encode($datos);
		}
		else
		{
			$datos["estado"]  = 0;
			$datos["mensaje"] = "Ha ocurrido un error";
			print json_encode($datos);
		}
	}

	//test link
	// http://cpmx.claresti.com/modificar_cuenta.php?id=23&nombre=Ahorro&descripcion=Ahorros (modificado)


?>
