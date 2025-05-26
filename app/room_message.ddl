create table public.room_message (
  room_id character varying(50) not null,
  send_user_id character varying(50) not null,
  send_user_icon_path character varying(200),
  main_message character varying(500),
  create_at timestamp(6) without time zone
);
