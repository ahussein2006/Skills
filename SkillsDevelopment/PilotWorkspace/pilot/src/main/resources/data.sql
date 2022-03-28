delete from Taco_Order_Tacos;
delete from Taco_Ingredients;
delete from Taco;
delete from Taco_Order;

delete from Ingredient;
insert into Ingredient (id, code, name, type) 
                values (1, 'FLTO', 'Flour Tortilla', 'WRAP');
insert into Ingredient (id, code, name, type)  
                values (2, 'COTO', 'Corn Tortilla', 'WRAP');
insert into Ingredient (id, code, name, type)  
                values (3, 'GRBF', 'Ground Beef', 'PROTEIN');
insert into Ingredient (id, code, name, type)  
                values (4, 'CARN', 'Carnitas', 'PROTEIN');
insert into Ingredient (id, code, name, type)  
                values (5, 'TMTO', 'Diced Tomatoes', 'VEGGIES');
insert into Ingredient (id, code, name, type)  
                values (6, 'LETC', 'Lettuce', 'VEGGIES');
insert into Ingredient (id, code, name, type)  
                values (7, 'CHED', 'Cheddar', 'CHEESE');
insert into Ingredient (id, code, name, type)  
                values (8, 'JACK', 'Monterrey Jack', 'CHEESE');
insert into Ingredient (id, code, name, type)  
                values (9, 'SLSA', 'Salsa', 'SAUCE');
insert into Ingredient (id, code, name, type)  
                values (10, 'SRCR', 'Sour Cream', 'SAUCE');
                
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(5,'Test Taco 5',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(10,'Test Taco 10',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(20,'Test Taco 20',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(30,'Test Taco 30',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(40,'Test Taco 40',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(50,'Test Taco 50',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(60,'Test Taco 60',sysdate,0);
INSERT INTO TACO("ID","NAME","INSERTEDAT","VERSION")VALUES(70,'Test Taco 70',sysdate,0);
