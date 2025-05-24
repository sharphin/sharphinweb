create table public.posts (
  post_id character varying(50) not null,
  poster_id character varying(50) not null,
  create_at time without time zone,
  primary key (post_id)
);
