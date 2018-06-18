INSERT INTO client_details (client_id, client_secret, client_name, dynamically_registered, refresh_token_validity_seconds, access_token_validity_seconds, id_token_validity_seconds, allow_introspection) VALUES
  ('client', 'secret', 'Test Client', false, null, 3600, 600, true);

INSERT INTO client_scope (owner_id, scope) VALUES
  ('1', 'openid'),
  ('1', 'profile'),
  ('1', 'email'),
  ('1', 'address'),
  ('1', 'phone'),
  ('1', 'offline_access');

INSERT INTO client_redirect_uri (owner_id, redirect_uri) VALUES
  ('1', 'https://www.getpostman.com/oauth2/callback'),
  ('1', 'http://ns357509.ip-91-121-149.eu:3001/oidc_callback'),
  ('1', 'http://localhost/'),
  ('1', 'http://localhost:8080/');

INSERT INTO client_grant_type (owner_id, grant_type) VALUES
  ('1', 'authorization_code'),
  ('1', 'urn:ietf:params:oauth:grant_type:redelegate'),
  ('1', 'implicit'),
  ('1', 'refresh_token');


INSERT INTO system_scope (scope, description, icon, restricted, default_scope, structured, structured_param_description) VALUES
  ('openid', 'log in using your identity', 'user', false, true, false, null),
  ('profile', 'basic profile information', 'list-alt', false, true, false, null),
  ('email', 'email address', 'envelope', false, true, false, null),
  ('address', 'physical address', 'home', false, true, false, null),
  ('phone', 'telephone number', 'bell', false, true, false, null),
  ('offline_access', 'offline access', 'time', false, false, false, null);

INSERT INTO users (username, password, enabled) VALUES
  ('admin','password',true),
  ('user','password',true);


INSERT INTO authorities (username, authority) VALUES
  ('admin','ROLE_ADMIN'),
  ('admin','ROLE_USER'),
  ('user','ROLE_USER');

-- By default, the username column here has to match the username column in the users table, above
INSERT INTO user_info (sub, preferred_username, name, email, email_verified, phone_number, phone_number_verified) VALUES
  ('90342.ASDFJWFA','admin','Demo Admin','admin@example.com', false, '+33608273008', true),
  ('01921.FLANRJQW','user','Demo User','user@example.com', true, '+33617514306', true);

