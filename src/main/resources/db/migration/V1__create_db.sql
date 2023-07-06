create table if not exists client(
    ID INT auto_increment,
    primary key(id),
    name varchar (200) not null,
    constraint check_name check (length (name)>2)
);
create table if not exists planet(
    ID varchar not null,
    primary key(id),
    name varchar (500) not null
);
create table if not exists ticket(
    id int auto_increment,
    primary key(id),
    created_at date not null,
    client_id int,
    foreign key (client_id) references client(id),
    from_planet_id varchar,
    foreign key (from_planet_id) references planet(id),
    to_planet_id varchar,
    foreign key (to_planet_id) references planet(id)
);