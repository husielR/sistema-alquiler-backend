--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

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

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: contratoestado; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.contratoestado AS ENUM (
    'Activo',
    'Anulado',
    'Finalizado',
    'Incumplido'
);


--
-- Name: estado_contrato; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.estado_contrato AS ENUM (
    'Activo',
    'Finalizado',
    'Incumplido',
    'Anulado'
);


--
-- Name: estado_pago; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.estado_pago AS ENUM (
    'Pendiente',
    'Pagado',
    'Atrasado',
    'Anulado'
);


--
-- Name: estado_propiedad; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.estado_propiedad AS ENUM (
    'Disponible',
    'Ocupado',
    'Mantenimiento'
);


--
-- Name: pagoestado; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.pagoestado AS ENUM (
    'Anulado',
    'Atrasado',
    'Pagado',
    'Pendiente'
);


--
-- Name: pagotipopago; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.pagotipopago AS ENUM (
    'Deuda',
    'Garantia',
    'Mensualidad',
    'Penalidad'
);


--
-- Name: propiedadestado; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.propiedadestado AS ENUM (
    'Disponible',
    'Mantenimiento',
    'Ocupado'
);


--
-- Name: tipo_pago_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.tipo_pago_enum AS ENUM (
    'Mensualidad',
    'Garantia',
    'Penalidad',
    'Deuda'
);


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cliente; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cliente (
    id_cliente integer NOT NULL,
    dni_ce character varying(255) NOT NULL,
    nombres character varying(255) NOT NULL,
    apellidos character varying(255) NOT NULL,
    telefono character varying(255),
    email character varying(255),
    contacto_emergencia character varying(255)
);


--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cliente_id_cliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cliente_id_cliente_seq OWNED BY public.cliente.id_cliente;


--
-- Name: contrato; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contrato (
    id_contrato integer NOT NULL,
    id_cliente integer NOT NULL,
    id_propiedad integer NOT NULL,
    fecha_inicio date NOT NULL,
    fecha_fin date,
    monto_garantia double precision NOT NULL,
    monto_mensual double precision NOT NULL,
    dia_pago integer NOT NULL,
    estado public.estado_contrato DEFAULT 'Activo'::public.estado_contrato
);


--
-- Name: contrato_id_contrato_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contrato_id_contrato_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contrato_id_contrato_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contrato_id_contrato_seq OWNED BY public.contrato.id_contrato;


--
-- Name: pago; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pago (
    id_pago integer NOT NULL,
    id_contrato integer NOT NULL,
    periodo_mes integer NOT NULL,
    periodo_anio integer NOT NULL,
    monto_pagado double precision NOT NULL,
    fecha_pago date,
    fecha_vencimiento date NOT NULL,
    estado public.estado_pago DEFAULT 'Pendiente'::public.estado_pago,
    tipo_pago public.tipo_pago_enum DEFAULT 'Mensualidad'::public.tipo_pago_enum
);


--
-- Name: pago_id_pago_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pago_id_pago_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pago_id_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pago_id_pago_seq OWNED BY public.pago.id_pago;


--
-- Name: propiedad; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.propiedad (
    id_propiedad integer NOT NULL,
    id_tipo integer NOT NULL,
    identificador character varying(255) NOT NULL,
    precio_base double precision NOT NULL,
    estado public.estado_propiedad DEFAULT 'Disponible'::public.estado_propiedad,
    id_ubicacion integer
);


--
-- Name: propiedad_id_propiedad_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.propiedad_id_propiedad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: propiedad_id_propiedad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.propiedad_id_propiedad_seq OWNED BY public.propiedad.id_propiedad;


--
-- Name: tipo_propiedad; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_propiedad (
    id_tipo integer NOT NULL,
    nombre character varying(255) NOT NULL,
    descripcion character varying(255)
);


--
-- Name: tipo_propiedad_id_tipo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tipo_propiedad_id_tipo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tipo_propiedad_id_tipo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tipo_propiedad_id_tipo_seq OWNED BY public.tipo_propiedad.id_tipo;


--
-- Name: ubicacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ubicacion (
    id_ubicacion integer NOT NULL,
    nombre character varying(255) NOT NULL,
    direccion character varying(255),
    ciudad character varying(255) DEFAULT 'Pisco'::character varying
);


--
-- Name: ubicacion_id_ubicacion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ubicacion_id_ubicacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ubicacion_id_ubicacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ubicacion_id_ubicacion_seq OWNED BY public.ubicacion.id_ubicacion;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    password character varying(255) NOT NULL,
    rol character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    CONSTRAINT usuario_rol_check CHECK (((rol)::text = ANY ((ARRAY['ADMIN'::character varying, 'ENCARGADO'::character varying])::text[])))
);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.usuario ALTER COLUMN id_usuario ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.usuario_id_usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: usuario_ubicacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario_ubicacion (
    id_usuario integer NOT NULL,
    id_ubicacion integer NOT NULL
);


--
-- Name: cliente id_cliente; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente ALTER COLUMN id_cliente SET DEFAULT nextval('public.cliente_id_cliente_seq'::regclass);


--
-- Name: contrato id_contrato; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contrato ALTER COLUMN id_contrato SET DEFAULT nextval('public.contrato_id_contrato_seq'::regclass);


--
-- Name: pago id_pago; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago ALTER COLUMN id_pago SET DEFAULT nextval('public.pago_id_pago_seq'::regclass);


--
-- Name: propiedad id_propiedad; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.propiedad ALTER COLUMN id_propiedad SET DEFAULT nextval('public.propiedad_id_propiedad_seq'::regclass);


--
-- Name: tipo_propiedad id_tipo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_propiedad ALTER COLUMN id_tipo SET DEFAULT nextval('public.tipo_propiedad_id_tipo_seq'::regclass);


--
-- Name: ubicacion id_ubicacion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion ALTER COLUMN id_ubicacion SET DEFAULT nextval('public.ubicacion_id_ubicacion_seq'::regclass);


--
-- Name: cliente cliente_dni_ce_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_dni_ce_key UNIQUE (dni_ce);


--
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- Name: contrato contrato_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contrato
    ADD CONSTRAINT contrato_pkey PRIMARY KEY (id_contrato);


--
-- Name: pago pago_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT pago_pkey PRIMARY KEY (id_pago);


--
-- Name: propiedad propiedad_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.propiedad
    ADD CONSTRAINT propiedad_pkey PRIMARY KEY (id_propiedad);


--
-- Name: tipo_propiedad tipo_propiedad_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_propiedad
    ADD CONSTRAINT tipo_propiedad_pkey PRIMARY KEY (id_tipo);


--
-- Name: ubicacion ubicacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion
    ADD CONSTRAINT ubicacion_pkey PRIMARY KEY (id_ubicacion);


--
-- Name: usuario uk863n1y3x0jalatoir4325ehal; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT uk863n1y3x0jalatoir4325ehal UNIQUE (username);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: usuario_ubicacion fk7kve540egaffurmaqb538mshb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_ubicacion
    ADD CONSTRAINT fk7kve540egaffurmaqb538mshb FOREIGN KEY (id_ubicacion) REFERENCES public.ubicacion(id_ubicacion);


--
-- Name: usuario_ubicacion fk9ydta012mro0m0kd7sq3lnkae; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_ubicacion
    ADD CONSTRAINT fk9ydta012mro0m0kd7sq3lnkae FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- Name: contrato fk_cliente; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contrato
    ADD CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);


--
-- Name: pago fk_contrato; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago
    ADD CONSTRAINT fk_contrato FOREIGN KEY (id_contrato) REFERENCES public.contrato(id_contrato);


--
-- Name: contrato fk_propiedad; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contrato
    ADD CONSTRAINT fk_propiedad FOREIGN KEY (id_propiedad) REFERENCES public.propiedad(id_propiedad);


--
-- Name: propiedad fk_tipo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.propiedad
    ADD CONSTRAINT fk_tipo FOREIGN KEY (id_tipo) REFERENCES public.tipo_propiedad(id_tipo);


--
-- Name: propiedad fk_ubicacion; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.propiedad
    ADD CONSTRAINT fk_ubicacion FOREIGN KEY (id_ubicacion) REFERENCES public.ubicacion(id_ubicacion);


--
-- PostgreSQL database dump complete
--

