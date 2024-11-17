CREATE TABLE public.tables
(
    id      BIGSERIAL PRIMARY KEY,
    number  INT NOT NULL,
    status  VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE'
);
ALTER TABLE public.orders ADD COLUMN table_id BIGINT;
ALTER TABLE public.orders ADD CONSTRAINT fk_table FOREIGN KEY (table_id) REFERENCES public.tables(id);
