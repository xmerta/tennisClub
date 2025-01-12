INSERT INTO surface_types (id, name, price_per_minute)
VALUES (1, 'Grass', 1.0),
       (2, 'Lava', 1.5);

INSERT INTO courts (id, name, surface_type_id)
VALUES (1, 'Court A', 1 ),
       (2, 'Court 2', 1 ),
       (3, 'Court III', 2),
       (4, 'Court DELTA', 2);
