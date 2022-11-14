CREATE TABLE IF NOT EXISTS events
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    description character varying(500) NOT NULL,
    organizer character varying(100) NOT NULL,
    topic character varying(100) NOT NULL,
    event_time timestamp without time zone NOT NULL,
    place character varying(100) NOT NULL,
    CONSTRAINT events_pkey PRIMARY KEY (id)
    )