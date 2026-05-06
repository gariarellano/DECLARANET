### Schema de GraphQL

``` GraphQL
####   I_DOMICILIO_DECLARANTE

"I Domicilio del declarante"
input DomicilioDeclarante {
	domicilio: Domicilio!
	aclaracionesObservaciones: String
	verificar:Boolean
}

```