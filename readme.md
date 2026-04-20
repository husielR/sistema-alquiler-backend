Otro modal a agregar es
ModalCobrar (Para registrar el ingreso de dinero):
Botón disparador: Un nuevo botón 'Cobrar' (con ícono de billete) al lado del lápiz, solo visible si el estado es 'Pendiente' o 'Atrasado'.
Encabezado Informativo: Un 'Badge' o texto destacado: 'Cobrando [Tipo] de [Mes/Año] - Deuda Total: $[Monto]'. Ninguno es editable.
Campo Habilitado: 'Fecha de Pago' (Datepicker, por defecto la fecha de hoy).
La Lógica del Checkbox ('¿Es pago parcial?'):
Si está APAGADO (Pago completo): Solo muestra la 'Fecha de Pago'. Al guardar, envía un payload con la fecha a la API de pago completo (PUT http://localhost:9085/apialquiler/pago/{{id}}/pagar).
Si está ENCENDIDO (Pago parcial): Despliega 'Monto que entrega hoy' y 'Días de plazo para el saldo'. Al guardar, llama a la API de pago parcial (POST http://localhost:9085/apialquiler/pago/{{id}}/pago-parcial).