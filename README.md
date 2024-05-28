1- Lanzar la ETL desde JAVA con el RUN

2- Lanzar la url de lectura de archivos que guarda en la tabla INTERFACE mediante esta url:
    localhost:8080/appInsurance/v1/readInfoFileNEGO/{resource}?codprov=CODIGO_PROVEEDOR&date=DATE
    donde CODIGO_PROVEEDOR debera ser cualquier proveedor que tengamos y DATE el dia que indiquemos en formato YYYYmmDD.
    Estos dos son opcionales por eso tienen esa estructura

3- Una vez integradro en la interface lanzar:
    localhost:8080/appInsurance/v1/processInfoFileNEGO/{resource}?codprov=CODIGO_PROVEEDOR&date=DATE
    donde CODIGO_PROVEEDOR debera ser cualquier proveedor que tengamos y DATE el dia que indiquemosen formato YYYYmmDD.
    Estos dos son opcionales por eso tienen esa estructura

4- Una vez lanzado esto lanzar la url:
    localhost:8080/appInsurance/v1/genInvoiceFileNEGO/{resource}?date=DATE
    DATE el dia que indiquemos en formato YYYYMM

5- Una vez hecho esto se habra creado las facturas correspondientes
