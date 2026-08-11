-- SQL Script to set up RANKS database tables in Supabase

-- 1. Profiles Table
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
    full_name TEXT NOT NULL,
    target_exam TEXT NOT NULL,
    class_grade TEXT NOT NULL,
    setup_complete BOOLEAN DEFAULT TRUE
);

-- Set up Row Level Security (RLS) for the profiles table
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;

-- Allow authenticated users to insert their own profile
DROP POLICY IF EXISTS "Users can insert their own profile" ON public.profiles;
CREATE POLICY "Users can insert their own profile" ON public.profiles
    FOR INSERT WITH CHECK (auth.uid() = id);

-- Allow authenticated users to view their own profile
DROP POLICY IF EXISTS "Users can view their own profile" ON public.profiles;
CREATE POLICY "Users can view their own profile" ON public.profiles
    FOR SELECT USING (auth.uid() = id);

-- Allow authenticated users to update their own profile
DROP POLICY IF EXISTS "Users can update their own profile" ON public.profiles;
CREATE POLICY "Users can update their own profile" ON public.profiles
    FOR UPDATE USING (auth.uid() = id);


-- 2. Questions Table
-- Assuming it already exists but might need updating, here is the full schema:
CREATE TABLE IF NOT EXISTS public.questions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    questiontext TEXT NOT NULL,
    exam TEXT NOT NULL DEFAULT 'Unknown',
    subject TEXT NOT NULL DEFAULT 'Unknown',
    chapter TEXT NOT NULL DEFAULT 'Unknown',
    topic TEXT NOT NULL DEFAULT 'Unknown',
    difficulty Int NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- Set up Row Level Security (RLS) for the questions table
ALTER TABLE public.questions ENABLE ROW LEVEL SECURITY;

-- Allow authenticated users to insert their own questions
DROP POLICY IF EXISTS "Users can insert their own questions" ON public.questions;
CREATE POLICY "Users can insert their own questions" ON public.questions
    FOR INSERT WITH CHECK (auth.uid() = user_id);

-- Allow authenticated users to view their own questions
DROP POLICY IF EXISTS "Users can view their own questions" ON public.questions;
CREATE POLICY "Users can view their own questions" ON public.questions
    FOR SELECT USING (auth.uid() = user_id);

-- Allow authenticated users to update their own questions
DROP POLICY IF EXISTS "Users can update their own questions" ON public.questions;
CREATE POLICY "Users can update their own questions" ON public.questions
    FOR UPDATE USING (auth.uid() = user_id);

-- Allow authenticated users to delete their own questions
DROP POLICY IF EXISTS "Users can delete their own questions" ON public.questions;
CREATE POLICY "Users can delete their own questions" ON public.questions
    FOR DELETE USING (auth.uid() = user_id);

-- Note: If the questions table already exists, just run these ALTER TABLE commands:
-- ALTER TABLE public.questions ADD COLUMN exam TEXT DEFAULT 'Unknown';
-- ALTER TABLE public.questions ADD COLUMN chapter TEXT DEFAULT 'Unknown';
-- ALTER TABLE public.questions ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now());
