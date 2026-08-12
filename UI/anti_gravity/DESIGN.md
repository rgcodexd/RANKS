---
name: Anti-Gravity
colors:
  surface: '#0b1326'
  surface-dim: '#0b1326'
  surface-bright: '#31394d'
  surface-container-lowest: '#060e20'
  surface-container-low: '#131b2e'
  surface-container: '#171f33'
  surface-container-high: '#222a3d'
  surface-container-highest: '#2d3449'
  on-surface: '#dae2fd'
  on-surface-variant: '#c7c4d7'
  inverse-surface: '#dae2fd'
  inverse-on-surface: '#283044'
  outline: '#908fa0'
  outline-variant: '#464554'
  surface-tint: '#c0c1ff'
  primary: '#c0c1ff'
  on-primary: '#1000a9'
  primary-container: '#8083ff'
  on-primary-container: '#0d0096'
  inverse-primary: '#494bd6'
  secondary: '#4fdbc8'
  on-secondary: '#003731'
  secondary-container: '#04b4a2'
  on-secondary-container: '#003f38'
  tertiary: '#ffb95f'
  on-tertiary: '#472a00'
  tertiary-container: '#ca8100'
  on-tertiary-container: '#3e2400'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#e1e0ff'
  primary-fixed-dim: '#c0c1ff'
  on-primary-fixed: '#07006c'
  on-primary-fixed-variant: '#2f2ebe'
  secondary-fixed: '#71f8e4'
  secondary-fixed-dim: '#4fdbc8'
  on-secondary-fixed: '#00201c'
  on-secondary-fixed-variant: '#005048'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#0b1326'
  on-background: '#dae2fd'
  surface-variant: '#2d3449'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '800'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '700'
    lineHeight: 28px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 16px
  margin-mobile: 20px
  margin-tablet: 40px
---

## Brand & Style

The design system is built on the narrative of "Anti-Gravity"—defying the weight of traditional education through lightness, elevation, and cosmic ambition. It targets high-performance students who view exam preparation as a mission rather than a chore.

The visual style is a fusion of **Modern Corporate** and **Glassmorphism**. It utilizes deep space aesthetics—not as a heavy, dark void, but as a vibrant, high-tech environment. Interfaces should feel like a futuristic cockpit: precise, informative, and ethereal. Key brand attributes include:
- **Weightlessness:** High use of elevation, backdrop blurs, and floating containers.
- **Precision:** Clean geometric lines and structured data visualization for complex analytics.
- **Momentum:** Subtle glows and "stellar" accents that guide the eye toward progress and completion.

## Colors

The palette is anchored in deep cosmic tones to minimize eye strain during long study sessions.

- **Gravity Purple (#6366F1):** The primary engine of the UI. Used for primary actions, progress indicators, and brand-heavy moments.
- **Stellar Teal (#14B8A6):** Used for "Success" states, secondary navigation, and interactive hints.
- **Nova Orange (#F59E0B):** A high-energy accent for notifications, urgent deadlines, and streaks.
- **Cosmic Neutrals:** The background uses a near-black `020617` to provide infinite depth, while surfaces use `1E293B` with semi-transparency to create the "floating" effect.

## Typography

The design system uses **Plus Jakarta Sans** exclusively to maintain a modern, friendly, yet highly legible geometric feel. 

- **Headlines:** Use Bold and ExtraBold weights with tighter letter spacing to create a sense of impact and "mass."
- **Body:** Use Regular weight with generous line height (1.5x) to ensure readability during dense question sets.
- **Labels:** Use SemiBold for buttons and metadata to ensure they remain legible against translucent backgrounds.

## Layout & Spacing

This design system employs a **Fluid Grid** model built on a 4px baseline rhythm.

- **Mobile (Default):** A 4-column layout with 20px outside margins. This creates a focused, vertically-driven study flow.
- **Tablet/Landscape:** Transitions to an 8 or 12-column grid. Complex data (like leaderboard analytics) should expand horizontally, while question content remains centered in a narrowed container to maintain an optimal line length.
- **Padding:** Internal card padding should be a minimum of `lg` (24px) to reinforce the "Anti-Gravity" sense of space and openness.

## Elevation & Depth

Visual hierarchy is achieved through **Glassmorphism** and **Tonal Layering**. 

1. **The Void (Base):** Deep black/navy background.
2. **Floating Surfaces:** Cards and containers use a subtle `surface_hex` with 80% opacity and a `16px` background blur.
3. **Glow States:** Active elements (like the current question or a selected answer) do not use traditional shadows. Instead, they use a soft `12px` outer glow tinted with the element's primary color (`primary_color_hex` at 20% opacity).
4. **Rim Lighting:** High-elevation components (modals/drawers) should feature a 1px top-border (linear gradient from white at 20% to transparent) to simulate light hitting the edge from a "distant star."

## Shapes

The shape language is consistently **Rounded**, reflecting a soft, approachable high-tech feel.

- **Primary Containers:** Standardized at `16px` (rounded-lg) for cards and input fields.
- **Action Elements:** Buttons and interactive chips use `rounded-xl` or full pill shapes to signify "touchability."
- **Interactive States:** On press, elements should subtly scale down (98%) to provide tactile feedback, emphasizing the "anti-gravity" responsiveness.

## Components

- **Buttons:** Primary buttons feature a subtle horizontal gradient (Gravity Purple to a slightly lighter indigo). Secondary buttons use an "outline-glass" style—transparent background with a 1.5px border and backdrop blur.
- **Cards:** Question cards must have high contrast. Use a clear separation between the "Stem" (the question) and "Options" (the answers) using a subtle vertical divider or tonal shift.
- **Chips:** Used for "Subject Tags" (e.g., Physics, Calculus). These should be pill-shaped with a low-opacity fill of the primary color.
- **Input Fields:** Search and text inputs should use a semi-transparent dark fill with a 1px border that glows when focused.
- **Progress Bars:** Use a "Glow-track" style—the unfilled portion is dark and recessed, while the filled portion has a trailing light effect.
- **Stellar HUD (New):** A specialized header component for students that shows their "Current Rank" and "Daily Streak" using "Nova Orange" accents and micro-animations.