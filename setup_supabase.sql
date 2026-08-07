-- SQL Script to set up RANKS database tables in Supabase

-- 1. Profiles Table
CREATE TABLE public.profiles (
    id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    full_name TEXT NOT NULL,
    target_exam TEXT NOT NULL,
    class_grade TEXT NOT NULL,
    setup_complete BOOLEAN DEFAULT TRUE
);

-- 2. Questions Table
-- Assuming it already exists but might need updating, here is the full schema:
CREATE TABLE IF NOT EXISTS public.questions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    questionText TEXT NOT NULL,
    exam TEXT NOT NULL DEFAULT 'Unknown',
    subject TEXT NOT NULL DEFAULT 'Unknown',
    chapter TEXT NOT NULL DEFAULT 'Unknown',
    topic TEXT NOT NULL DEFAULT 'Unknown',
    difficulty Int NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Note: If the questions table already exists, just run these ALTER TABLE commands:
-- ALTER TABLE public.questions ADD COLUMN exam TEXT DEFAULT 'Unknown';
-- ALTER TABLE public.questions ADD COLUMN chapter TEXT DEFAULT 'Unknown';
-- ALTER TABLE public.questions ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now());
