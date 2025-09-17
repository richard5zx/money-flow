#money-flow

## Frontend

## Backend

## Database (postgreSQL)
Script to create tables necessary for this project.
script
-- =========================================================
-- Extensions (once per database)
-- =========================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;  -- gen_random_uuid(), crypt(), etc.
CREATE EXTENSION IF NOT EXISTS citext;    -- case-insensitive text

-- =========================================================
-- Helper: auto-update updated_at on any UPDATE
-- =========================================================
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS trigger AS $$
BEGIN
  NEW.updated_at := now();
  RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- =========================================================
-- DROP (dev convenience) – safe to remove in prod migrations
-- =========================================================
DROP TABLE IF EXISTS expenses      CASCADE;
DROP TABLE IF EXISTS subcategories CASCADE;
DROP TABLE IF EXISTS categories    CASCADE;
DROP TABLE IF EXISTS users         CASCADE;

-- =========================================================
-- USERS
-- =========================================================
CREATE TABLE users (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email          CITEXT UNIQUE NOT NULL,      -- case-insensitive
  password_hash  TEXT NOT NULL,               -- hash in your Java backend
  full_name      TEXT,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TRIGGER users_set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =========================================================
-- CATEGORIES (top level)
-- =========================================================
CREATE TABLE categories (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name        TEXT UNIQUE NOT NULL,           -- e.g., 'Transport', 'Food & Dining'
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TRIGGER categories_set_updated_at
BEFORE UPDATE ON categories
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =========================================================
-- SUBCATEGORIES (must belong to a category)
-- =========================================================
CREATE TABLE subcategories (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  category_id  UUID NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
  name         TEXT NOT NULL,                 -- e.g., 'Fuel', 'Car Servicing'
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_subcategory UNIQUE (category_id, name)  -- no dup subcats under same category
);

CREATE TRIGGER subcategories_set_updated_at
BEFORE UPDATE ON subcategories
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =========================================================
-- EXPENSES (subcategory REQUIRED)
-- =========================================================
CREATE TABLE expenses (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id        UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  subcategory_id UUID NOT NULL REFERENCES subcategories(id) ON DELETE RESTRICT,
  occurred_on    DATE NOT NULL,                            -- date spent
  amount         NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
  note           TEXT,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TRIGGER expenses_set_updated_at
BEFORE UPDATE ON expenses
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Helpful indexes for the large/active table
CREATE INDEX idx_expenses_user_date   ON expenses(user_id, occurred_on);
CREATE INDEX idx_expenses_subcategory ON expenses(subcategory_id);
```
