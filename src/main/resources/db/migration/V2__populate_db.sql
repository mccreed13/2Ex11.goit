insert into client (name) values
  ('Gregory'),
  ('John'),
  ('Jenny'),
  ('Ivan'),
  ('Anton'),
  ('Mike'),
  ('Michael'),
  ('Alex'),
  ('Connor'),
  ('Like');

insert into planet (id, name) values
  ('MARS','Mars'),
  ('VEN','Venus'),
  ('SAT','Saturn'),
  ('JUP','Jupiter'),
  ('I21','Iden');

insert into ticket (created_at, client_id, from_planet_id, to_planet_id) values
  ('2007-02-03', 1, 'MARS', 'VEN'),
  ('2010-01-10', 2, 'JUP', 'MARS'),
  ('2020-12-02', 3, 'MARS', 'I21'),
  ('2011-02-12', 4, 'I21', 'JUP'),
  ('2012-02-11', 3, 'MARS', 'SAT'),
  ('2013-02-09', 5, 'JUP', 'VEN'),
  ('2007-02-05', 1, 'MARS', 'JUP'),
  ('2004-02-08', 3, 'I21', 'JUP'),
  ('2011-02-02', 4, 'MARS', 'SAT'),
  ('2022-02-01', 2, 'SAT', 'VEN'),
  ('2008-02-07', 1, 'I21', 'VEN');