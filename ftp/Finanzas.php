<?php
	require 'Database.php';
	/**
	*
	* Funciones WS
	*
	*/

	class Finanzas
	{
		function __construct()
		{

		}

		//INSERTS

		/**
	     *	Insertar un usuario a la Base de Datos
	     *	Parámetros
	     *	@nombre nombre del usuario
	     *	@username username del usuario a registrar
	     *	@password contraseña del usuario a registrar
	     *	@correo correo del usuario a registrar
	     *	@sexo sexo del usuario a registrar
	     *	@fnacimiento fecha de nacimiento del usuario a registrar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function insertar_usuario($nombre, $username, $password, $correo, $sexo, $fnacimiento)
		{

			if(isset($nombre) && !empty($nombre) && isset($username) && !empty($username) && isset($password) && !empty($password) && isset($correo) && !empty($correo) && isset($sexo) && !empty($sexo) && isset($fnacimiento) && !empty($fnacimiento))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$comando = "INSERT INTO root (Usuario, Nombre, Password, Correo, Sexo, Fecha_Nac) 
				VALUES (
				'$username',
				'$nombre',
				'$password',
				'$correo',
				'$sexo',
				'$fnacimiento')";
				print $comando;

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);
				
				return $sentencia -> execute(
					array(
						$username,
						$nombre,
						$password,
						$correo,
						$sexo,
						$fnacimiento
						)
					);
			}
			else
			{	
				
				return 0;
			}
		}


		/**
	     *	Insertar una cuenta por default a la base de datos
	     *	Parámetros
	     *	@nombre nombre del usuario
	     *	@username username del usuario a registrar
	     *	@password contraseña del usuario a registrar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function crearCuentaUsuarioNuevo($username)
		{
			if(isset($username) && !empty($username))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$id = Finanzas::getIdUser($username);
				$id = $id[0]['idUsuario'];

				$comando = "INSERT INTO cuenta (Nombre, Descripcion, idUsuario) 
				VALUES (
				'Mi Cuenta',
				'Cuenta por Defecto',
				$id)";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						'Mi Cuenta',
						'Cuenta por Defecto',
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}

		/**
	     *	Insertar una cuenta por default a la base de datos
	     *	Parámetros
	     *	@nombre nombre del usuario
	     *	@username username del usuario a registrar
	     *	@password contraseña del usuario a registrar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function crearCategoriaUsuarioNuevo($username)
		{
			if(isset($username) && !empty($username))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$id = Finanzas::getIdUser($username);
				$id = $id[0]['idUsuario'];

				$comando = "INSERT INTO categoria (Nombre, Descripcion, idUsuario) 
				VALUES (
				'Sin Categoria',
				'Categoria por Defecto',
				$id)";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						'Sin Categoria',
						'Categoria por Defecto',
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}


		/**
		* Funcion para crear la cuenta por defecto de cada usuario al registrarse
		* $nombre - nombre de la cuenta
		* $descripcion -  descripcion de la cuenta
		* $username - nombre de usuario al que se le asignara la cuenta
		* @return valor de confirmaciion
		*/
		public static function crearCuenta($nombre, $descripcion, $username)
		{
			if(isset($username) && !empty($username) && 
				isset($descripcion) && !empty($descripcion) &&
				isset($nombre) && !empty($nombre))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$id = Finanzas::getIdUser($username);
				$id = $id[0]['idUsuario'];

				$comando = "INSERT INTO cuenta (Nombre, Descripcion, idUsuario) 
				VALUES (
				'$nombre',
				'$descripcion'
				$id)";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$nombre,
						$descripcion,
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}


		//UPDATES

		/**
	     *	Modificar un usuario ya registrado
	     *	Parámetros
	     *	@id id del usuario a modificar
	     *	@nombre nuevo nombre del usuario
	     *	@username nuevo username del usuario
	     *	@password nueva contraseña del usuario
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function modificar_usuario($id, $nombre, $password, $sexo, $fnacimiento)
		{
			if(isset($id) && !empty($id) && isset($nombre) && !empty($nombre) && isset($password) && !empty($password) && isset($sexo) && !empty($sexo) && isset($fnacimiento) && !empty($fnacimiento))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$comando = "UPDATE root SET Nombre = '$nombre', Password = '$password', Sexo = '$sexo', Fecha_Nac = '$fnacimiento' WHERE idUsuario = '$id'";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$nombre,
						$password,
						$sexo,
						$fnacimiento
						)
					);
			}
			else
			{
				return 0;
			}
		}

		/**
	     *	Modificar una categoria ya registrada
	     *	Parámetros
	     *	@id id de la catgoria a modificar
	     *	@nombre nuevo nombre de la categoria
	     *	@descripcion descripcion de la categoria a modificar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function modificarCategoria($id, $nombre, $descripcion)
		{
			if(isset($id) && !empty($id) && isset($nombre) && !empty($nombre) && isset($descripcion) && !empty($descripcion))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$comando = "UPDATE categoria SET Nombre = '$nombre', Descripcion = '$descripcion' WHERE ID = '$id'";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$nombre,
						$descripcion,
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}

		/**
	     *	Modificar una cuenta ya registrada
	     *	Parámetros
	     *	@id id de la cuenta a modificar
	     *	@nombre nuevo nombre de la cuenta
	     *	@descripcion descripcion de la cuenta a modificar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function modificarCuenta($id, $nombre, $descripcion)
		{
			if(isset($id) && !empty($id) && isset($nombre) && !empty($nombre) && isset($descripcion) && !empty($descripcion))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$comando = "UPDATE cuenta SET Nombre = '$nombre', Descripcion = '$descripcion' WHERE ID = '$id'";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$nombre,
						$descripcion,
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}

		/**
	     *	Modificar un movimiento ya registrado
	     *	Parámetros
	     *	@id id de la cuenta a modificar
	     *	@nombre nuevo nombre de la cuenta
	     *	@descripcion descripcion de la cuenta a modificar
	     *	Salidas
	     *	0 - en caso de no hacer nada
	     *	1 - json con informacion de la consulta
	     */
		public static function modificarMovimiento($id, $monto, $tipo, $fecha)
		{
			if(isset($id) && !empty($id) && isset($monto) && !empty($monto) && isset($tipo) && !empty($tipo) && isset($fecha) && !empty($fecha))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$comando = "UPDATE movimiento SET Monto = $monto, Tipo = $tipo, fecha = '$fecha' WHERE ID = '$id'";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$tipo,
						$fecha,
						$fecha,
						$id
						)
					);
			}
			else
			{
				return 0;
			}
		}


		//DELETES

		/**
	     *	Elimina un usuario de la Base de Datos
	     * $id - id del usuario a eliminar
	     * @return valor booleano dependiendo si la operacion se realizo con exito o no 
	     */
		public static function eliminar_usuario($id)
		{
			$consulta = "DELETE FROM root WHERE idUsuario = '$id'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);

				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		/**
	     *	Elimina una categoria
	     * $id - id de la categoria a eliminar
	     * @return valor booleano dependiendo si la operacion se realizo con exito o no 
	     */
		public static function eliminar_categoria($id)
		{
			$consulta = "DELETE FROM categoria WHERE ID = '$id'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);

				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		/**
	     * Elimina un movimiento
	     * $id - id del movimiento a eliminar
	     * @return valor booleano dependiendo si la operacion se realizo con exito o no 
	     */
		public static function eliminar_movimiento($id)
		{
			$consulta = "DELETE FROM movimiento WHERE ID = '$id'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);

				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		/**
	     * Elimina una cuenta
	     * $id - id de la cuenta a eliminar
	     * @return valor booleano dependiendo si la operacion se realizo con exito o no 
	     */
		public static function eliminar_cuenta($id)
		{
			$consulta = "DELETE FROM cuenta WHERE ID = '$id'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);

				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}


		//SELECTS

		/**
		* Funcion para obtener todos los usuarios existentes
		* @return arreglo con todos los usuarios existententes en la base de datos
		*/
		public static function getUsuarios()
		{
			$consulta = "SELECT * FROM root";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}


		/**
		* Funcion para obtener las cuentas de un usuario
		* $username - nombre de usuario
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function getCuentasById($idUsuario)
		{

			if(isset($idUsuario) && !empty($idUsuario))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$consulta = "SELECT ID, Nombre FROM cuenta WHERE idUsuario = ".$idUsuario;

				try
				{
					$comando = Database::getInstance() -> getDb() -> prepare($consulta);
					$comando -> execute();
					return $comando -> fetchAll(PDO::FETCH_ASSOC);
				}
				catch(PDOException $e)
				{
					return 0;
				}
			}else{
				return 0;
			}
		}

		

		public static function getCategoriasById($idUsuario)
		{

			if(isset($idUsuario) && !empty($idUsuario))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$consulta = "SELECT ID, Nombre FROM categoria WHERE idUsuario = ".$idUsuario;

				try
				{
					$comando = Database::getInstance() -> getDb() -> prepare($consulta);
					$comando -> execute();
					return $comando -> fetchAll(PDO::FETCH_ASSOC);
				}
				catch(PDOException $e)
				{
					return 0;
				}
			}else{
				return 0;
			}
		}




		/**
		* Funcion para saber si existen usuarios
		* $identificador correo o usuario para login
		* $password password del usuario
		* @return usuarios que coincidan con los parametros de entrada
		*/
		public static function getUsuariosLogin($identificador, $password)
		{
			$consulta = "SELECT count(Correo) as total from root where Correo = '$identificador' or Usuario = '$identificador' and Password = '$password'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para extraer usuarios con identificador
		* $identificador correo o usuario para login
		* $password password del usuario
		* @return usuarios que coincidan con los parametros de entrada
		*/
		public static function getUsuario($identificador, $password)
		{
			$consulta = "SELECT idUsuario, Usuario, Nombre, Correo, Sexo, Fecha_Nac from root where Correo = '$identificador' or Usuario = '$identificador' and Password = '$password'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para saber si existe un usuario
		* $username - nombre de usuario
		* @return cantidad de usuarios que coincidan con ese nombre de usuario
		*/
		public static function getExistsUser($username)
		{
			$consulta = "SELECT count(Correo) as total from root where Usuario = '$username'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para saber si existe un correo ya registrado
		* $correo - correo del usuario
		* @return cantidad de cuentas que existen con ese correo
		*/
		public static function getExistsMail($correo)
		{
			$consulta = "SELECT count(Correo) as total from root where Correo = '$correo'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener el is de un usuario
		* $idUsuario - ID del usuario
		* @return id del usuario que concida
		*/
		public static function getIdUser($username)
		{
			$consulta = "SELECT idUsuario from root where Usuario = '$username'";
			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para saber si existe una categoria con el nombre
		* $idUsuario - ID del usuario
		* $nombre - Nombre de la cuenta
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getExistsCat($idUsuario, $nombre)
		{
			$consulta = "SELECT count(Nombre) as total from categoria where idUsuario = '$idUsuario' and Nombre = '$nombre'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para saber si existe una cuenta con el nombre
		* $idUsuario - ID del usuario
		* $nombre - Nombre de la cuenta
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getExistsCuenta($idUsuario, $nombre)
		{
			$consulta = "SELECT count(Nombre) as total from cuenta where idUsuario = '$idUsuario' and Nombre = '$nombre'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos de los ultimos 30 dias
		* $idUsuario - ID del usuario
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovsemana($idUsuario)
		{
			// $consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= CURRENT_DATE() and fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY)";
			$consulta = "SELECT movimiento.ID,movimiento.Monto,movimiento.Tipo,movimiento.fecha,movimiento.idCategoria,movimiento.idCuenta,movimiento.idUsuario, categoria.Nombre from movimiento INNER JOIN categoria on movimiento.idCategoria = categoria.ID where movimiento.idUsuario = '$idUsuario' and fecha <= CURRENT_DATE() and fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY)";
			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos en un intervalo de tiempo
		* $idUsuario - ID del usuario
		* $finicio - fecha de inicio
		* $ftermino - fecha de termino
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovintervalo($idUsuario, $finicio, $ftermino)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= '$ftermino' and fecha >= '$finicio'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos de los ultimos 30 dias por categoria
		* $idUsuario - ID del usuario
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovcatsemana($idUsuario, $idCategoria)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= CURRENT_DATE() and fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) and idCategoria = $idCategoria";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos en un intervalo de tiempo
		* $idUsuario - ID del usuario
		* $finicio - fecha de inicio
		* $ftermino - fecha de termino
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovcatintervalo($idUsuario, $finicio, $ftermino, $idCategoria)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= '$ftermino' and fecha >= '$finicio' and idCategoria = $idCategoria";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos de los ultimos 30 dias por cuenta
		* $idUsuario - ID del usuario
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovcuesemana($idUsuario, $idCuenta)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= CURRENT_DATE() and fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) and idCuenta = $idCuenta";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos en un intervalo de tiempo
		* $idUsuario - ID del usuario
		* $finicio - fecha de inicio
		* $ftermino - fecha de termino
		* $idCuenta - cuenta a filtrar
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovcueintervalo($idUsuario, $finicio, $ftermino, $idCuenta)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= '$ftermino' and fecha >= '$finicio' and idCuenta = $idCuenta";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos de los ultimos 30 dias por tipo de movimiento
		* $idUsuario - ID del usuario
		* $tipo - tipo de movimiento
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovtiposemana($idUsuario, $tipo)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= CURRENT_DATE() and fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY) and Tipo = $tipo";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos en un intervalo de tiempo
		* $idUsuario - ID del usuario
		* $finicio - fecha de inicio
		* $ftermino - fecha de termino
		* $tipo - tipo de movimiento
		* @return cantidad de Cuentas que coincidan con los parametros dados
		*/
		public static function getmovtipointervalo($idUsuario, $finicio, $ftermino, $tipo)
		{
			$consulta = "SELECT * from movimiento where idUsuario = '$idUsuario' and fecha <= '$ftermino' and fecha >= '$finicio' and Tipo = $tipo";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}




		/**
		* Funcion para obtener los datos usados para las gráficas de Cuentas
		* $idUsuario - ID del usuario
		* @return nombres de las categorías y total de categorias del usuario
		*/
		public static function graficaCuentas($idUsuario)
		{
			//$consulta  = "SELECT COUNT(*) as cantidad from cuenta where idUsuario = '$idUsuario'";
			$consulta2 = "SELECT cuenta.Nombre, movimiento.monto from cuenta join movimiento ON cuenta.ID = movimiento.idCuenta where movimiento.idUsuario = '$idUsuario' AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

			try
			{
				// //comando para obtener el número de cuentas de cada usuario
				// $comando = Database::getInstance() -> getDb() -> prepare($consulta);
				// $comando -> execute();
				// $comando = $comando -> fetchAll(PDO::FETCH_ASSOC);
				
				
				$comando2 = Database::getInstance() -> getDb() -> prepare($consulta2);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

				//return [$comando,$comando2];
				return $comando2;
				
			}
			catch(PDOException $e)
			{
				return false;
			}
		}


		/**
		* Funcion para obtener los datos usados para las gráficas de Cuentas
		* $idUsuario - ID del usuario
		* @return nombres de las categorías y total de categorias del usuario
		*/
		public static function graficaCategorias($idUsuario)
		{

			$consulta2 = "SELECT categoria.Nombre, movimiento.monto from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

			try
			{
				$comando2 = Database::getInstance() -> getDb() -> prepare($consulta2);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

				return $comando2;
				
			}
			catch(PDOException $e)
			{
				return false;
			}
		}


		public static function graficaCategoriasRango($idUsuario, $inicio, $fin)
		{
			$consulta2 = "SELECT categoria.Nombre, SUM(movimiento.monto) as monto from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.fecha BETWEEN '$inicio' AND '$fin' group by categoria.Nombre";
			try
			{

				$comando2 = Database::getInstance() -> getDb() -> prepare($consulta2);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

				return $comando2;
				
			}
			catch(PDOException $e)
			{
				return false;
			}
		}


		/**
		* Funcion para obtener los datos usados para las estadisticsa de Categorias
		* $idUsuario - ID del usuario
		* $filtro - mensual o semanal
		* @return todos los datos que se obtengan según el filtro
		*/
		public static function statsCategorias($idUsuario)
		{
				$consultaI = "SELECT SUM(Monto) as Ingresos from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 1 AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

				$consultaE  = "SELECT SUM(Monto) as Egresos from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 2 AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consultaI);
				$comando -> execute();
				$comando = $comando -> fetchAll(PDO::FETCH_ASSOC);		

				$comando2 = Database::getInstance() -> getDb() -> prepare($consultaE);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

   				
   				foreach ($comando as $row) {
				    $aux1 = $row["Ingresos"];
				}
				foreach ($comando2 as $row) {
				    $aux2 = $row["Egresos"];
				}

				$aux = $aux1 - $aux2;
				$comando3 =["Balance" => $aux];
				$asdasd = array_merge($comando, $comando2,$comando3);

				return $asdasd;


			}
			catch(PDOException $e)
			{
				return 0;
			}
		}



		/**
		* Funcion para obtener los datos usados para las estadisticsa de Cuentas
		* $idUsuario - ID del usuario
		* $filtro - mensual o semanal
		* @return todos los datos que se obtengan según el filtro
		*/
		public static function statsCuentas($idUsuario)
		{
				$consultaI = "SELECT SUM(Monto) as Ingresos from cuenta join movimiento ON cuenta.ID = movimiento.idCuenta where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 1 AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

				$consultaE  = "SELECT SUM(Monto) as Egresos from cuenta join movimiento ON cuenta.ID = movimiento.idCuenta where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 2 AND movimiento.fecha >= (DATE_SUB(CURDATE(), INTERVAL 1 MONTH))";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consultaI);
				$comando -> execute();
				$comando = $comando -> fetchAll(PDO::FETCH_ASSOC);		

				$comando2 = Database::getInstance() -> getDb() -> prepare($consultaE);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);
   				
   				foreach ($comando as $row) {
				    $aux1 = $row["Ingresos"];
				}
				foreach ($comando2 as $row) {
				    $aux2 = $row["Egresos"];
				}

				$aux = $aux1 - $aux2;
				$comando3 = ["Balance:" => $aux];


				return array_merge($comando, $comando2);


			}
			catch(PDOException $e)
			{
				return 0;
			}
		}


		/**
		* CON RANGO
		* Funcion para obtener los datos usados para las estadisticsa de Categorias
		* $idUsuario - ID del usuario
		* $filtro - mensual o semanal
		* @return todos los datos que se obtengan según el filtro
		*/
		public static function statsCategoriasRango($idUsuario, $inicio, $fin)
		{
				$consultaI = "SELECT SUM(Monto) as Ingresos from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 1 AND movimiento.fecha BETWEEN '$inicio' AND '$fin'";


				$consultaE  = "SELECT SUM(Monto) as Egresos from categoria join movimiento ON categoria.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 2 AND movimiento.fecha BETWEEN '$inicio' AND '$fin'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consultaI);
				$comando -> execute();
				$comando = $comando -> fetchAll(PDO::FETCH_ASSOC);		

				$comando2 = Database::getInstance() -> getDb() -> prepare($consultaE);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

   				
   				foreach ($comando as $row) {
				    $aux1 = $row["Ingresos"];
				}
				foreach ($comando2 as $row) {
				    $aux2 = $row["Egresos"];
				}

				$aux = $aux1 - $aux2;
				$comando3 = ["Balance:", $aux];


				return array_merge($comando, $comando2);


			}
			catch(PDOException $e)
			{
				return 0;
			}
		}



		/**
		* Funcion para obtener los datos usados para las estadisticsa de Cuentas
		* CON RANGO
		* $idUsuario - ID del usuario
		* $filtro - mensual o semanal
		* @return todos los datos que se obtengan según el filtro
		*/
		public static function statsCuentasRango($idUsuario, $inicio, $fin)
		{
				$consultaI = "SELECT SUM(Monto) as Ingresos from cuenta join movimiento ON cuenta.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 1 AND movimiento.fecha BETWEEN '$inicio' AND '$fin'";

				$consultaE = "SELECT SUM(Monto) as Egresos from cuenta join movimiento ON cuenta.ID = movimiento.idCategoria where movimiento.idUsuario = '$idUsuario' AND movimiento.Tipo = 2 AND movimiento.fecha BETWEEN '$inicio' AND '$fin'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consultaI);
				$comando -> execute();
				$comando = $comando -> fetchAll(PDO::FETCH_ASSOC);		

				$comando2 = Database::getInstance() -> getDb() -> prepare($consultaE);
				$comando2 -> execute();
				$comando2 = $comando2 -> fetchAll(PDO::FETCH_ASSOC);

   				
   				foreach ($comando as $row) {
				    $aux1 = $row["Ingresos"];
				}
				foreach ($comando2 as $row) {
				    $aux2 = $row["Egresos"];
				}

				$aux = $aux1 - $aux2;
				$comando3 = ["Balance:", $aux];


				return array_merge($comando, $comando2);


			}
			catch(PDOException $e)
			{
				return 0;
			}
		}


		public static function getEgresosIngresos($idCategoria, $tipo)
		{
			$consulta = "select SUM(movimiento.Monto) as egresos from movimiento where idCategoria = $idCategoria and Tipo = $tipo";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		public static function getEgresosIngresosFecha($idCategoria, $tipo, $fin, $fte)
		{
			$consulta = "select SUM(movimiento.Monto) as egresos from movimiento where idCategoria = $idCategoria and Tipo = $tipo and fecha >= '$fin' and fecha <= '$fte'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		public static function getEgresosIngresosCuenta($idCuenta, $tipo)
		{
			$consulta = "select SUM(movimiento.Monto) as egresos from movimiento where idCuenta = $idCuenta and Tipo = $tipo";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}

		public static function getEgresosIngresosFechaCuenta($idCuenta, $tipo, $fin, $fte)
		{
			$consulta = "select SUM(movimiento.Monto) as egresos from movimiento where idCuenta = $idCuenta and Tipo = $tipo and fecha >= '$fin' and fecha <= '$fte'";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return false;
			}
		}



		/**
		* ORDERED FUNCTIONS
		*/

		/**
		*SELECTS
		*/

		//MOVEMENTS

		/**
		* Funcion para obtener todas los movimientos de un usuario
		* $username - Nombre de usuario
		* @return arreglo con las cuentas de un usuario
		*/

		public static function getMovements($idUser)
		{
			$query = "SELECT movimiento.ID,movimiento.Monto,movimiento.Tipo,movimiento.fecha,movimiento.idCategoria,movimiento.idCuenta,movimiento.idUsuario, movimiento.concepto, movimiento.idCuentaTransfer from movimiento where movimiento.idUsuario = $idUser ORDER BY movimiento.ID DESC LIMIT 0, 15";

			try
			{
				$com = Database::getInstance() -> getDb() -> prepare($query);
				$com -> execute();
				return $com -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $pdoe)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener los movimientos de un usuario
		* $idUser - id del usuario
		* $minId - id minimo para cargar
		* @return arreglo con las cuentas de un usuario
		*/

		public static function getMoreMovements($idUser, $minId)
		{
			$query = "SELECT * from movimiento where movimiento.idUsuario = $idUser and movimiento.ID < $minId LIMIT 0, 15";

			try
			{
				$com = Database::getInstance() -> getDb() -> prepare($query);
				$com -> execute();
				return $com -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $pdoe)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener la cantidad de movimientos de un usuario
		* $idUser - id del usuario
		* $minId - id minimo para cargar
		* @return arreglo con las cuentas de un usuario
		*/

		public static function getQuantityOfMovements($idUser, $minId)
		{
			$query = "SELECT count(*) As quantity from movimiento where movimiento.idUsuario = $idUser and movimiento.ID < $minId LIMIT 0, 15";

			try
			{
				$com = Database::getInstance() -> getDb() -> prepare($query);
				$com -> execute();
				return $com -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $pdoe)
			{
				return false;
			}
		}

		/**
		* Funcion para obtener un movimiento en especifico
		* $idMovement - Id del movimiento 
		* @return arreglo con un movimiento
		*/

		public static function getMovement($idMovement)
		{
			$query = "SELECT * from movimiento where ID=$idMovement";

			try
			{
				$com = Database::getInstance() -> getDb() -> prepare($query);
				$com -> execute();
				return $com -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $pdoe)
			{
				return false;
			}
		}

		//CATEGORIES

		/**
		* Funcion para obtener todas las categorias de un usuario
		* $username - Nombre de usuario
		* @return arreglo con las cuentas de un usuario
		*/
		public static function getCategorias($username)
		{

			if(isset($username) && !empty($username))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$id = Finanzas::getIdUser($username);
				$id = $id[0]['idUsuario'];

				$consulta = "SELECT ID, Nombre, Descripcion FROM categoria WHERE idUsuario = ".$id;

				try
				{
					$comando = Database::getInstance() -> getDb() -> prepare($consulta);
					$comando -> execute();
					return $comando -> fetchAll(PDO::FETCH_ASSOC);
				}
				catch(PDOException $e)
				{
					return 0;
				}
			}else{
				return 0;
			}
		}

		//ACCOUNTS

		/**
		* Funcion para obtener las cuentas de un usuario
		* $username - nombre de usuario
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function getCuentas($username)
		{

			if(isset($username) && !empty($username))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$id = Finanzas::getIdUser($username);
				$id = $id[0]['idUsuario'];

				$consulta = "SELECT ID, Nombre, Descripcion, dinero FROM cuenta WHERE idUsuario = ".$id;

				try
				{
					$comando = Database::getInstance() -> getDb() -> prepare($consulta);
					$comando -> execute();
					return $comando -> fetchAll(PDO::FETCH_ASSOC);
				}
				catch(PDOException $e)
				{
					return 0;
				}
			}else{
				return 0;
			}
		}

		/**
		* Funcion para obtener el dinero de alguna cuenta de un usuario
		* $idCuenta - id de la cuenta
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function getDineroCuenta($idCuenta)
		{

			if(isset($idCuenta) && !empty($idCuenta))
			{
				$body = json_decode(file_get_contents("php://input"), true);

				$consulta = "SELECT dinero FROM cuenta WHERE ID = ".$idCuenta;

				try
				{
					$comando = Database::getInstance() -> getDb() -> prepare($consulta);
					$comando -> execute();
					return $comando -> fetchAll(PDO::FETCH_ASSOC);
				}
				catch(PDOException $e)
				{
					return 0;
				}
			}else{
				return 0;
			}
		}





		/**
		* INSERTS
		**/

		//MOVEMENTS

		/**
		* Funcion para hacer un nuevo movimiento de ingreso o egreso
		* $idUsuario - ID del usuario
		* $idCategoria - ID de la categoria a la que pertenece el movimiento (puede ser null)
		* $monto - monto de el movimiento
		* $idCuenta - id de la cuenta en la que se hara el movimiento
		* $tipo - tipo de movimiento 1 - ingreso, 0 - egreso
		* @return valor booleano de confirmacion
		*/
		public static function nuevoMovimiento($idUsuario, $idCategoria, $monto, $idCuenta, $tipo, $fecha, $concepto)
		{
			$body = json_decode(file_get_contents("php://input"), true);

			$comando = "INSERT INTO movimiento (idUsuario, idCategoria, Monto, idCuenta, Tipo, fecha, concepto) 
			VALUES (
			$idUsuario,
			$idCategoria,
			$monto,
			$idCuenta,
			'$tipo',
			'$fecha',
			'$concepto')";

			$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

			return $sentencia -> execute(
				array(
					$idUsuario,
					$idCategoria,
					$monto,
					$idCuenta,
					$tipo,
					$fecha,
					$concepto
					)
				);
			
		}


		/**
		* Funcion para hacer un nuevo movimiento de transferencia
		* $idUsuario - ID del usuario
		* $idCategoria - ID de la categoria a la que pertenece el movimiento (puede ser null)
		* $monto - monto de el movimiento
		* $idCuenta - id de la cuenta en la que se hara el movimiento
		* $tipo - tipo de movimiento 1 - ingreso, 0 - egreso
		* @return valor booleano de confirmacion
		*/
		public static function nuevoMovimientoTransfer($idUsuario, $idCategoria, $monto, $idCuenta, $tipo, $fecha, $concepto, $idCuentaTransfer)
		{
			$body = json_decode(file_get_contents("php://input"), true);

			$comando = "INSERT INTO movimiento (idUsuario, idCategoria, Monto, idCuenta, Tipo, fecha, concepto, idCuentaTransfer) 
			VALUES (
			$idUsuario,
			$idCategoria,
			$monto,
			$idCuenta,
			'$tipo',
			'$fecha',
			'$concepto',
			$idCuentaTransfer)";

			$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

			return $sentencia -> execute(
				array(
					$idUsuario,
					$idCategoria,
					$monto,
					$idCuenta,
					$tipo,
					$fecha,
					$concepto,
					$idCuentaTransfer
					)
				);
			
		}

		//CATEGORIES

		/**
		* Funcion para registrar una categoria
		* $idUsuario - ID del usuario
		* $nombre - Nombre de la categoria
		* $descripcion - Descripcion de la categoria
		* @return valor booleano de confirmacion
		*/
		public static function nuevaCategoria($idUsuario, $nombre, $descripcion)
		{
			$body = json_decode(file_get_contents("php://input"), true);

			$comando = "INSERT INTO categoria (idUsuario, Nombre, Descripcion) 
			VALUES (
			$idUsuario,
			'$nombre',
			'$descripcion')";

			$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

			return $sentencia -> execute(
				array(
					$idUsuario,
					$nombre,
					$descripcion
					)
				);
		}

		//ACCOUNTS

		/**
		* Funcion para crear la cuenta
		* $nombre - nombre de la cuenta
		* $descripcion -  descripcion de la cuenta
		* $username - nombre de usuario al que se le asignara la cuenta
		* @return valor de confirmaciion
		*/
		public static function nuevaCuenta($idUsuario, $nombre, $descripcion)
		{
			$body = json_decode(file_get_contents("php://input"), true);

				$comando = "INSERT INTO cuenta (Nombre, Descripcion, idUsuario) 
				VALUES (
				'$nombre',
				'$descripcion',
				$idUsuario)";

				$sentencia = Database::getInstance() -> getDb() -> prepare($comando);

				return $sentencia -> execute(
					array(
						$nombre,
						$descripcion,
						$idUsuario
						)
					);
		}


		/**
		* UPDATES
		**/

		//MOVEMENTS

		/**
		* Funcion para modificar los datos de un movimiento (Ingreso, Egreso)
		* $idCuenta - id de la cuenta
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function modifyMovement($idMovement, $idCategory, $amount, $idAccount, $type, $concept)
		{

			$body = json_decode(file_get_contents("php://input"), true);

			$consulta = "Update movimiento set idCategoria=$idCategory, Monto=$amount, idCuenta=$idAccount, Tipo=$type, concepto='$concept', idCuentaTransfer=null WHERE ID =$idMovement";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		/**
		* Funcion para modificar los datos de un movimiento (Transferencia)
		* $idCuenta - id de la cuenta
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function modifyMovementTransfer($idMovement, $idCategory, $amount, $idAccount, $type, $concept, $idAccountTransfer)
		{

			$body = json_decode(file_get_contents("php://input"), true);

			$consulta = "Update movimiento set idCategoria=$idCategory, Monto=$amount, idCuenta=$idAccount, Tipo=$type, concepto='$concept', idCuentaTransfer=$idCuentaTransfer WHERE ID =$idMovement";

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return 1;
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		//CATEGORIES

		//ACCOUNTS

		/**
		* Funcion para restar dinero a una cuenta
		* $idCuenta - id de la cuenta
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function restarDineroCuenta($idCuenta, $monto)
		{

			$body = json_decode(file_get_contents("php://input"), true);

			$consulta = "Update cuenta set dinero=dinero - ".$monto." WHERE ID = ".$idCuenta;

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}

		/**
		* Funcion para sumar dinero a una cuenta
		* $idCuenta - id de la cuenta
		* @return arreglo con todas las cuentas de un usuario
		*/
		public static function sumarDineroCuenta($idCuenta, $monto)
		{

			$body = json_decode(file_get_contents("php://input"), true);

			$consulta = "Update cuenta set dinero=dinero + ".$monto." WHERE ID = ".$idCuenta;

			try
			{
				$comando = Database::getInstance() -> getDb() -> prepare($consulta);
				$comando -> execute();
				return $comando -> fetchAll(PDO::FETCH_ASSOC);
			}
			catch(PDOException $e)
			{
				return 0;
			}
		}



	}
?>