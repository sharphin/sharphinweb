create table public.room_members (
  room_id character varying(50) not null,
  room_member_id character varying(50) not null,
  member_auth character varying(10) not null,
  create_at timestamp(6) without time zone not null,
  primary key (room_id, room_member_id)
);
