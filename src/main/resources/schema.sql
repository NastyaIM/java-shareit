drop table IF EXISTS items CASCADE;
drop table IF EXISTS bookings CASCADE;
drop table IF EXISTS comments CASCADE;
drop table IF EXISTS requests CASCADE;
drop table IF EXISTS users CASCADE;

create table if not exists users (
  id bigint GENERATED always AS IDENTITY NOT NULL,
  name_user varchar(255) not null,
  email varchar(512) not null,
  constraint pk_user primary key (id),
  constraint UQ_USER_EMAIL unique (email)
);

create table if not exists requests (
  id bigint GENERATED always AS IDENTITY NOT NULL,
  description varchar(300) not null,
  requester_id bigint,
  created TIMESTAMP WITHOUT TIME ZONE,
  constraint pk_requests primary key (id),
  constraint fk_requests_to_users foreign key(requester_id) references users(id)
);

create table if not exists items (
  id bigint GENERATED always AS IDENTITY NOT NULL,
  name_item varchar(255) not null,
  description varchar(300) not null,
  available boolean not null,
  owner_id bigint,
  request_id bigint,
  constraint pk_item primary key (id),
  constraint fk_items_to_users foreign key(owner_id) references users(id),
  constraint fk_items_to_requests foreign key(request_id) references requests(id)
);

create table if not exists bookings (
  id bigint GENERATED always AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE,
  end_date TIMESTAMP WITHOUT TIME ZONE,
  item_id bigint,
  booker_id bigint,
  status varchar,
  constraint pk_bookings primary key (id),
  constraint fk_bookings_to_users foreign key(booker_id) references users(id),
  constraint fk_bookings_to_items foreign key(item_id) references items(id)
);

create table if not exists comments (
  id bigint GENERATED always AS IDENTITY NOT NULL,
  text varchar not null,
  item_id bigint,
  author_id bigint,
  created TIMESTAMP WITHOUT TIME ZONE,
  constraint pk_comments primary key (id),
  constraint fk_comments_to_items foreign key(item_id) references items(id),
  constraint fk_comments_to_users foreign key(author_id) references users(id)
);