create table public.users (
  user_id character varying(100) not null,
  user_name character varying(100) not null,
  email character varying(100) not null,
  password character varying(100) not null,
  icon_path character varying(200),
  authority character varying(10) not null,
  disable boolean not null,
  create_at timestamp(6) without time zone not null,
  update_at timestamp(6) without time zone not null,
  primary key (user_id)
);
