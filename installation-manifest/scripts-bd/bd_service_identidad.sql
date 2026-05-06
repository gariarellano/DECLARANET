--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: ip_aplicacion; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_aplicacion (
    id_aplicacion integer NOT NULL,
    nombre character varying(30),
    secretkey character varying(30),
    url character varying(100),
    dominio character varying(100),
    obligatorio_curp_valida integer NOT NULL,
    firmado integer DEFAULT 0 NOT NULL,
    iss character varying(30),
    minutos integer DEFAULT 3
);


ALTER TABLE public.ip_aplicacion OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_aplicacion.id_aplicacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.id_aplicacion IS 'Identificador de la aplicación';


--
-- Name: COLUMN ip_aplicacion.nombre; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.nombre IS 'Nombre de la aplicación';


--
-- Name: COLUMN ip_aplicacion.secretkey; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.secretkey IS 'Secret key de la aplicación';


--
-- Name: COLUMN ip_aplicacion.url; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.url IS 'Url call back de la aplicación';


--
-- Name: COLUMN ip_aplicacion.dominio; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.dominio IS 'Dominio de la aplicación';


--
-- Name: COLUMN ip_aplicacion.obligatorio_curp_valida; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.obligatorio_curp_valida IS '0 = FALSE, 1 = TRUE';


--
-- Name: COLUMN ip_aplicacion.firmado; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.firmado IS 'Bandera que indica si usará la firma electronica';


--
-- Name: COLUMN ip_aplicacion.iss; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.iss IS 'Iss de Kong';


--
-- Name: COLUMN ip_aplicacion.minutos; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_aplicacion.minutos IS 'Tiempo para generar el token de sesión en kong';


--
-- Name: ip_aplicacion_id_aplicacion_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_aplicacion_id_aplicacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_aplicacion_id_aplicacion_seq OWNER TO identidadsfpusr;

--
-- Name: ip_aplicacion_id_aplicacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: identidadsfpusr
--

ALTER SEQUENCE public.ip_aplicacion_id_aplicacion_seq OWNED BY public.ip_aplicacion.id_aplicacion;


--
-- Name: ip_bitacora; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_bitacora (
    id_aplicacion integer,
    fecha character varying(30),
    curp character varying(18),
    accion integer,
    CONSTRAINT ip_bitacora_accion_check CHECK (((accion = 0) OR (accion = 1) OR (accion = 2) OR (accion = 3) OR (accion = 4) OR (accion = 5) OR (accion = 6) OR (accion = 7)))
);


ALTER TABLE public.ip_bitacora OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_bitacora.id_aplicacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_bitacora.id_aplicacion IS 'Id de la aplicación';


--
-- Name: COLUMN ip_bitacora.fecha; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_bitacora.fecha IS 'Fecha de la operación';


--
-- Name: COLUMN ip_bitacora.curp; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_bitacora.curp IS 'Curp de la operación';


--
-- Name: COLUMN ip_bitacora.accion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_bitacora.accion IS '0 = LOGUEO APLICACIÓN,
 1 = VINCULACION DE CUENTAS, 	
 2 = LOGUEO DE USUARIO,
 3 = ACCESO PROPORCIONADO (CURP), 
 4 = DESVINCULACIÓN DE CUENTAS,
 5 = REGISTRO USUARIO,
 6 = REGISTRO APLICACIÓN,
 7 = REESTABLECER PASSWORD';


--
-- Name: ip_confirmacion; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_confirmacion (
    id_confirmacion integer NOT NULL,
    id_usuario integer,
    fecha timestamp without time zone,
    token character varying(257),
    proceso integer,
    CONSTRAINT ip_confirmacion_proceso_check CHECK (((proceso = 0) OR (proceso = 1)))
);


ALTER TABLE public.ip_confirmacion OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_confirmacion.id_confirmacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_confirmacion.id_confirmacion IS 'Id confirmación';


--
-- Name: COLUMN ip_confirmacion.id_usuario; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_confirmacion.id_usuario IS 'Id del usuario que le corresponde la confirmación';


--
-- Name: COLUMN ip_confirmacion.fecha; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_confirmacion.fecha IS 'Fecha de generación la confirmación';


--
-- Name: COLUMN ip_confirmacion.token; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_confirmacion.token IS 'Token generado con la confirmación';


--
-- Name: COLUMN ip_confirmacion.proceso; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_confirmacion.proceso IS '0 = ACTIVACIÓN 1 = REESTABLECER PASSWORD';


--
-- Name: ip_confirmacion_id_confirmacion_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_confirmacion_id_confirmacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_confirmacion_id_confirmacion_seq OWNER TO identidadsfpusr;

--
-- Name: ip_confirmacion_id_confirmacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: identidadsfpusr
--

ALTER SEQUENCE public.ip_confirmacion_id_confirmacion_seq OWNED BY public.ip_confirmacion.id_confirmacion;


--
-- Name: ip_token_un_solo_uso; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_token_un_solo_uso (
    token character varying(800) NOT NULL,
    fecha_creacion date
);


ALTER TABLE public.ip_token_un_solo_uso OWNER TO identidadsfpusr;

--
-- Name: ip_transaccion; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_transaccion (
    id_transaccion integer NOT NULL,
    codigo_sha character varying(128),
    codigo_base64 character varying(512),
    codigo_token character varying(257),
    id_aplicacion integer,
    fecha character varying(30),
    curp character varying(18),
    id_usuario integer not null default 0
);


ALTER TABLE public.ip_transaccion OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_transaccion.id_transaccion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.id_transaccion IS 'Id transacción';


--
-- Name: COLUMN ip_transaccion.codigo_sha; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.codigo_sha IS 'Transacción encriptada';


--
-- Name: COLUMN ip_transaccion.codigo_base64; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.codigo_base64 IS 'Token, clienteId, curp y fecha en base 64';


--
-- Name: COLUMN ip_transaccion.codigo_token; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.codigo_token IS 'Token';


--
-- Name: COLUMN ip_transaccion.id_aplicacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.id_aplicacion IS 'Id aplicación';


--
-- Name: COLUMN ip_transaccion.fecha; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.fecha IS 'Fecha generación de la transacción';


--
-- Name: COLUMN ip_transaccion.curp; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_transaccion.curp IS 'Curp de la transacción';


--
-- Name: ip_transaccion_id_transaccion_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_transaccion_id_transaccion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_transaccion_id_transaccion_seq OWNER TO identidadsfpusr;

--
-- Name: ip_transaccion_id_transaccion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: identidadsfpusr
--

ALTER SEQUENCE public.ip_transaccion_id_transaccion_seq OWNED BY public.ip_transaccion.id_transaccion;


--
-- Name: ip_usuario; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_usuario (
    id_usuario integer NOT NULL,
    login character varying(15),
    pwd_enc character varying(40),
    nombre character varying(40),
    primer_apellido character varying(40),
    segundo_apellido character varying(40),
    curp character varying(20),
    rfc character(10),
    homocve character(3),
    num_celular character varying(20),
    email_alt character varying(100),
    email character varying(100),
    validado_renapo integer,
    fecha_registro timestamp without time zone,
    activo integer,
    CONSTRAINT ip_usuario_activo_check CHECK (((activo = 0) OR (activo = 1)))
);


ALTER TABLE public.ip_usuario OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_usuario.id_usuario; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.id_usuario IS 'Id usuario';


--
-- Name: COLUMN ip_usuario.login; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.login IS 'Login del usuario se genera con la secuencia ip_usuario_login_seq desde la app';


--
-- Name: COLUMN ip_usuario.pwd_enc; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.pwd_enc IS 'Password del usuario (login + password usuario encriptados)';


--
-- Name: COLUMN ip_usuario.nombre; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.nombre IS 'Nombre del usuario';


--
-- Name: COLUMN ip_usuario.primer_apellido; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.primer_apellido IS 'Primer apellido de usuario';


--
-- Name: COLUMN ip_usuario.segundo_apellido; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.segundo_apellido IS 'Segundo apellido de usuario';


--
-- Name: COLUMN ip_usuario.curp; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.curp IS 'Curp del usuario';


--
-- Name: COLUMN ip_usuario.rfc; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.rfc IS 'RFC del usuario';


--
-- Name: COLUMN ip_usuario.homocve; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.homocve IS 'Homoclave del rfc del usuario';


--
-- Name: COLUMN ip_usuario.num_celular; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.num_celular IS 'Numero celular del usuario';


--
-- Name: COLUMN ip_usuario.email_alt; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.email_alt IS 'Email alterno del usuario';


--
-- Name: COLUMN ip_usuario.email; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.email IS 'Email del usuario';


--
-- Name: COLUMN ip_usuario.validado_renapo; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.validado_renapo IS 'validado_renapo = 0 aun no validado validado_renapo = 1 Validado validado_renapo = 2 Curp inexistente validado_renapo = 3 Datos inconsistentes';


--
-- Name: COLUMN ip_usuario.fecha_registro; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.fecha_registro IS 'Fecha del registro del usuario';


--
-- Name: COLUMN ip_usuario.activo; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_usuario.activo IS '0 = INACTIVO, 1 = ACTIVO';


--
-- Name: ip_usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_usuario_id_usuario_seq OWNER TO identidadsfpusr;

--
-- Name: ip_usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: identidadsfpusr
--

ALTER SEQUENCE public.ip_usuario_id_usuario_seq OWNED BY public.ip_usuario.id_usuario;


--
-- Name: ip_usuario_login_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_usuario_login_seq
    START WITH 2000000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_usuario_login_seq OWNER TO identidadsfpusr;

--
-- Name: ip_vinculacion_usuario_aplicacion; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_vinculacion_usuario_aplicacion (
    id_vinculacion integer NOT NULL,
    id_usuario integer,
    id_aplicacion integer,
    fecha_vinculacion timestamp without time zone,
    activo_logico_vinculacion character(1),
    fecha_des_vinculacion timestamp without time zone
);


ALTER TABLE public.ip_vinculacion_usuario_aplicacion OWNER TO identidadsfpusr;

--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.id_vinculacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.id_vinculacion IS 'Id de la vinculación';


--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.id_usuario; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.id_usuario IS 'Id del usuario';


--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.id_aplicacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.id_aplicacion IS 'Id de la aplicación a la que se vinculó el usuario';


--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.fecha_vinculacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.fecha_vinculacion IS 'Fecha de la vinculación';


--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.activo_logico_vinculacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.activo_logico_vinculacion IS '1 = ACTIVO, 0 = INACTIVO';


--
-- Name: COLUMN ip_vinculacion_usuario_aplicacion.fecha_des_vinculacion; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON COLUMN public.ip_vinculacion_usuario_aplicacion.fecha_des_vinculacion IS 'Fecha de desvinculación del usuario';


--
-- Name: ip_notificaciones; Type: TABLE; Schema: public; Owner: identidadsfpusr
--

CREATE TABLE public.ip_notificaciones
(
    id_usuario integer NOT NULL,
    id_telegram double precision NOT NULL,
    telegram_activo integer NOT NULL,
    fecha_registro timestamp without time zone NOT NULL
);

ALTER TABLE public.ip_notificaciones OWNER to identidadsfpusr;


--
-- Name: ip_vinculacion_usuario_aplicacion_id_vinculacion_seq; Type: SEQUENCE; Schema: public; Owner: identidadsfpusr
--

CREATE SEQUENCE public.ip_vinculacion_usuario_aplicacion_id_vinculacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ip_vinculacion_usuario_aplicacion_id_vinculacion_seq OWNER TO identidadsfpusr;

--
-- Name: ip_vinculacion_usuario_aplicacion_id_vinculacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: identidadsfpusr
--

ALTER SEQUENCE public.ip_vinculacion_usuario_aplicacion_id_vinculacion_seq OWNED BY public.ip_vinculacion_usuario_aplicacion.id_vinculacion;


--
-- Name: ip_aplicacion id_aplicacion; Type: DEFAULT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_aplicacion ALTER COLUMN id_aplicacion SET DEFAULT nextval('public.ip_aplicacion_id_aplicacion_seq'::regclass);


--
-- Name: ip_confirmacion id_confirmacion; Type: DEFAULT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_confirmacion ALTER COLUMN id_confirmacion SET DEFAULT nextval('public.ip_confirmacion_id_confirmacion_seq'::regclass);


--
-- Name: ip_transaccion id_transaccion; Type: DEFAULT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_transaccion ALTER COLUMN id_transaccion SET DEFAULT nextval('public.ip_transaccion_id_transaccion_seq'::regclass);


--
-- Name: ip_usuario id_usuario; Type: DEFAULT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.ip_usuario_id_usuario_seq'::regclass);


--
-- Name: ip_vinculacion_usuario_aplicacion id_vinculacion; Type: DEFAULT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_vinculacion_usuario_aplicacion ALTER COLUMN id_vinculacion SET DEFAULT nextval('public.ip_vinculacion_usuario_aplicacion_id_vinculacion_seq'::regclass);


--
-- Name: ip_usuario id_usuario_unique; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_usuario
    ADD CONSTRAINT id_usuario_unique UNIQUE (curp);


--
-- Name: ip_aplicacion ip_aplicacion_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_aplicacion
    ADD CONSTRAINT ip_aplicacion_pkey PRIMARY KEY (id_aplicacion);


--
-- Name: ip_confirmacion ip_confirmacion_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_confirmacion
    ADD CONSTRAINT ip_confirmacion_pkey PRIMARY KEY (id_confirmacion);


--
-- Name: ip_confirmacion ip_confirmacion_token_key; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_confirmacion
    ADD CONSTRAINT ip_confirmacion_token_key UNIQUE (token);


--
-- Name: ip_token_un_solo_uso ip_token_un_solo_uso_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_token_un_solo_uso
    ADD CONSTRAINT ip_token_un_solo_uso_pkey PRIMARY KEY (token);


--
-- Name: ip_transaccion ip_transaccion_codigo_sha_key; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_transaccion
    ADD CONSTRAINT ip_transaccion_codigo_sha_key UNIQUE (codigo_sha);


--
-- Name: ip_transaccion ip_transaccion_codigo_token_key; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_transaccion
    ADD CONSTRAINT ip_transaccion_codigo_token_key UNIQUE (codigo_token);


--
-- Name: ip_transaccion ip_transaccion_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_transaccion
    ADD CONSTRAINT ip_transaccion_pkey PRIMARY KEY (id_transaccion);


--
-- Name: ip_aplicacion ip_usuario_obligatorio_curp_validada; Type: CHECK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE public.ip_aplicacion
    ADD CONSTRAINT ip_usuario_obligatorio_curp_validada CHECK (((obligatorio_curp_valida = 0) OR (obligatorio_curp_valida = 1))) NOT VALID;


--
-- Name: ip_usuario ip_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_usuario
    ADD CONSTRAINT ip_usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: ip_usuario ip_usuario_validado_renapo; Type: CHECK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE public.ip_usuario
    ADD CONSTRAINT ip_usuario_validado_renapo CHECK ((((validado_renapo)::text = '0'::text) OR ((validado_renapo)::text = '1'::text) OR ((validado_renapo)::text = '2'::text) OR ((validado_renapo)::text = '3'::text))) NOT VALID;


--
-- Name: CONSTRAINT ip_usuario_validado_renapo ON ip_usuario; Type: COMMENT; Schema: public; Owner: identidadsfpusr
--

COMMENT ON CONSTRAINT ip_usuario_validado_renapo ON public.ip_usuario IS 'validado_renapo = 0 Aun no validado
validado_renapo = 1 Validado
validado_renapo = 2 Curp inexistente
validado_renapo = 3 Datos inconsistentes';


--
-- Name: ip_vinculacion_usuario_aplicacion ip_vinculacion_usuario_aplicacion_pkey; Type: CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_vinculacion_usuario_aplicacion
    ADD CONSTRAINT ip_vinculacion_usuario_aplicacion_pkey PRIMARY KEY (id_vinculacion);


--
-- Name: idx_homocve; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX idx_homocve ON public.ip_usuario USING btree (homocve);


--
-- Name: idx_ipApp_App; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX "idx_ipApp_App" ON public.ip_aplicacion USING btree (id_aplicacion);


--
-- Name: idx_ipApp_AppSecrDom; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX "idx_ipApp_AppSecrDom" ON public.ip_aplicacion USING btree (id_aplicacion, secretkey, dominio);


--
-- Name: idx_ipUsr_CURP; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX "idx_ipUsr_CURP" ON public.ip_usuario USING btree (curp);


--
-- Name: idx_ipVinc_UsrApp; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX "idx_ipVinc_UsrApp" ON public.ip_vinculacion_usuario_aplicacion USING btree (id_usuario, id_aplicacion);


--
-- Name: idx_ip_transaccion_shatoken; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX idx_ip_transaccion_shatoken ON public.ip_transaccion USING btree (codigo_sha, codigo_token);


SET default_tablespace = ts_identidadsfp_dat;

--
-- Name: idx_ipbit_curp; Type: INDEX; Schema: public; Owner: identidadsfpusr; Tablespace: ts_identidadsfp_dat
--

CREATE INDEX idx_ipbit_curp ON public.ip_bitacora USING btree (curp);


--
-- Name: idx_ipbit_fecha; Type: INDEX; Schema: public; Owner: identidadsfpusr; Tablespace: ts_identidadsfp_dat
--

CREATE INDEX idx_ipbit_fecha ON public.ip_bitacora USING btree (fecha, accion);


SET default_tablespace = '';

--
-- Name: idx_rfc; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE INDEX idx_rfc ON public.ip_usuario USING btree (rfc);

--
-- Name: idx_ipNot_idTgram; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE UNIQUE INDEX "idx_ipNot_idTgram" ON public.ip_notificaciones USING btree
    (id_telegram ASC NULLS LAST)
    TABLESPACE ts_identidadsfp_dat;


--
-- Name: idx_ipNot_idusr; Type: INDEX; Schema: public; Owner: identidadsfpusr
--

CREATE UNIQUE INDEX "idx_ipNot_idusr"
    ON public.ip_notificaciones USING btree
    (id_usuario ASC NULLS LAST)
    TABLESPACE ts_identidadsfp_dat;


--
-- Name: ip_confirmacion ip_confirmacion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_confirmacion
    ADD CONSTRAINT ip_confirmacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.ip_usuario(id_usuario);


--
-- Name: ip_transaccion ip_transaccion_id_aplicacion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_transaccion
    ADD CONSTRAINT ip_transaccion_id_aplicacion_fkey FOREIGN KEY (id_aplicacion) REFERENCES public.ip_aplicacion(id_aplicacion);


--
-- Name: ip_vinculacion_usuario_aplicacion ip_vinculacion_usuario_aplicacion_id_aplicacion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_vinculacion_usuario_aplicacion
    ADD CONSTRAINT ip_vinculacion_usuario_aplicacion_id_aplicacion_fkey FOREIGN KEY (id_aplicacion) REFERENCES public.ip_aplicacion(id_aplicacion);


--
-- Name: ip_vinculacion_usuario_aplicacion ip_vinculacion_usuario_aplicacion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_vinculacion_usuario_aplicacion
    ADD CONSTRAINT ip_vinculacion_usuario_aplicacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.ip_usuario(id_usuario);

--
-- Name: ip_notificacion ip_notificacion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: identidadsfpusr
--

ALTER TABLE ONLY public.ip_notificaciones
    ADD CONSTRAINT ip_notificacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.ip_usuario (id_usuario);

--
-- PostgreSQL database dump complete
--
