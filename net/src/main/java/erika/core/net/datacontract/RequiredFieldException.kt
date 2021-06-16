package erika.core.net.datacontract

class RequiredFieldException(field: String = "") : Exception("Field $field is required")