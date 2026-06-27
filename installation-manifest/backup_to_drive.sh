#!/bin/bash
FECHA=$(date +%Y%m%d_%H%M%S)
DIR_BACKUP="/root/backups/temp_$FECHA"
ARCHIVO_TAR="/root/backups_declaranet_$FECHA.tar.gz"
ARCHIVO_ENCRIPTADO="${ARCHIVO_TAR}.gpg"

PASSWORD_ENCRIPTACION="Declar@Net2026!"
RCLONE_REMOTE="mi_drive"
RCLONE_DESTINO="$RCLONE_REMOTE:/Respaldos_Declaranet"

mkdir -p $DIR_BACKUP

docker exec catalogos bash -c "PGPASSWORD=userpass123 pg_dump -U catalogousr catalogos" > $DIR_BACKUP/backup_catalogos.sql
docker exec identidad bash -c "PGPASSWORD=userpasss123 pg_dump -U identidadsfpusr identidad" > $DIR_BACKUP/backup_identidad.sql
docker exec mondecla mongodump -u declausr -p usrpass123 --authenticationDatabase declaranet --archive > $DIR_BACKUP/backup_mondecla.archive
docker exec amaccesos mongodump -u amaccesosusr -p usrpass123 --authenticationDatabase amaccesos --archive > $DIR_BACKUP/backup_amaccesos.archive
docker exec dependencias mongodump -u dependenciausr -p usrpass123 --authenticationDatabase dependencia --archive > $DIR_BACKUP/backup_dependencias.archive
docker exec omext mongodump -u omextusr -p usrpass123 --authenticationDatabase omext --archive > $DIR_BACKUP/backup_omext.archive

cp /var/www/html/declaranet/installation-manifest/.env $DIR_BACKUP/backup_env_produccion.txt 2>/dev/null || true

tar -czf $ARCHIVO_TAR -C /root/backups temp_$FECHA
gpg --batch --yes --passphrase "$PASSWORD_ENCRIPTACION" -c $ARCHIVO_TAR
rclone copy $ARCHIVO_ENCRIPTADO $RCLONE_DESTINO

rm -rf $DIR_BACKUP
rm -f $ARCHIVO_TAR
find /root -maxdepth 1 -name "backups_declaranet_*.tar.gz.gpg" -type f -mtime +7 -delete
