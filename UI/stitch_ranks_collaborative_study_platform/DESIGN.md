---
name: Kinetic Neo-Academic
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#464555'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#777587'
  outline-variant: '#c7c4d8'
  surface-tint: '#4d44e3'
  primary: '#3525cd'
  on-primary: '#ffffff'
  primary-container: '#4f46e5'
  on-primary-container: '#dad7ff'
  inverse-primary: '#c3c0ff'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#684000'
  on-tertiary: '#ffffff'
  tertiary-container: '#885500'
  on-tertiary-container: '#ffd4a4'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e2dfff'
  primary-fixed-dim: '#c3c0ff'
  on-primary-fixed: '#0f0069'
  on-primary-fixed-variant: '#3323cc'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '800'
    lineHeight: 48px
    letterSpacing: -0.03em
  display-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 38px
    letterSpacing: -0.025em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.015em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: -0.01em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.01em
  mono-metric-lg:
    fontFamily: JetBrains Mono
    fontSize: 20px
    fontWeight: '700'
    lineHeight: 24px
    letterSpacing: -0.02em
  mono-metric-sm:
    fontFamily: JetBrains Mono
    fontSize: 13px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0em
  label-caps:
    fontFamily: JetBrains Mono
    fontSize: 11px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.08em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  spacing-2xs: 0.25rem
  spacing-xs: 0.5rem
  spacing-sm: 0.75rem
  spacing-md: 1rem
  spacing-lg: 1.25rem
  spacing-xl: 1.5rem
  spacing-2xl: 2rem
  spacing-3xl: 3rem
  mobile-margin: 1rem
  mobile-gutter: 0.75rem
  tablet-margin: 2rem
  tablet-gutter: 1rem
  desktop-max-width: 540px
---

## Brand & Style

This design system establishes a high-velocity, high-focus environment for crowdsourced academic mastery. It repudiates both dry, institutional textbook software and overly gamified, juvenile flashcard apps. Instead, the design merges the sleek precision of top-tier developer platforms with the electric drive of athletic competitive interfaces. 

The aesthetic is Modern High-Contrast with Electric Accents:
- **Atmosphere**: Crisp, surgical, luminous, and relentlessly forward-moving.
- **Target Persona**: Competitive STEM students, peer-collaborators, and rigorous exam candidates who balance rapid OCR question scanning with intense sprint sessions.
- **Emotional Intent**: Imparts an immediate sensation of academic capability, zero-latency feedback, competitive urgency, and deep mental clarity.

## Colors

The palette is engineered to direct ocular focus instantly across dense question sheets, answer breakdowns, and fast-moving leaderboards:

- **Primary (`#4F46E5` / Electric Violet `#6366F1`)**: Directs focal tasks, primary triggers, active tab selections, and scanning viewfinder vectors.
- **Secondary (`#10B981` / Emerald `#059669`)**: Dictates verified peer solutions, accuracy indices, completed sprints, and successful scan recognitions.
- **Tertiary (`#F59E0B` / Amber Gold)**: Reserved exclusively for prestige tiers, global ranks, active streaks, and leaderboard milestones.
- **Neutral & Canvas (`#0F172A`, `#F8FAFC`, `#FFFFFF`)**: A three-tier crisp architecture. Pristine white (`#FFFFFF`) forms foreground cards and surface elevations, rested over a cool slate base (`#F8FAFC`). Hairline strokes use `#E2E8F0` to prevent visual clutter in dense data structures.
- **System Accents**: Dark mode accents (`#020617` base with `#1E293B` containers) are reserved for low-light sprint modes and viewfinder overlays.

## Typography

The typographic hierarchy enforces immediate legibility through distinct structural assignments:

- **Display & Headlines (Plus Jakarta Sans)**: Utilized for view titles, user ranks, and motivational sprint hooks. Its geometric curves soften the structural rigidity of standard academic software without sacrificing authority.
- **Body & Editorial (Inter)**: Handles question prompts, crowdsourced rationales, and community threads. Tight, clean metrics maintain comfort through extended study blocks.
- **Data, Stats & Math Metadata (JetBrains Mono)**: Used for all timers, peer rankings, question reference numbers (e.g., `MATH.CALC.104`), score percentiles, and tabular comparisons. Tabular numbers prevent jitter during active timers and rapid tally animations.
- **LaTeX Rendering**: Rendered math equations align baseline-to-baseline with Inter body copy, utilizing matching font scale ratios to prevent vertical alignment jump.

## Layout & Spacing

This design system enforces a fluid, compact mobile-first layout based on a strict 4px base increment system:

- **Mobile Viewport (Base)**: Dynamic single-column stack with persistent `mobile-margin: 1rem` safe margins. Question flows and problem sets utilize strict vertical stacking with `spacing-sm` (8px) card gaps to maximize screen real estate above the fold.
- **Tablet & Desktop**: Tablet interfaces use an expandable dual-column format (left: question prompt & scanner viewfinder; right: verified steps, community comments, stats). Desktop rendering defaults to a centered, app-like native frame clamped at `desktop-max-width: 540px` to maintain focused ergonomics.
- **Rhythm & Touch Targets**: Primary actionable controls enforce a minimum touch container of 44x44px, padded internally using `spacing-sm` and `spacing-md` to avoid mis-taps during high-speed drills.

## Elevation & Depth

Depth is established via low-contrast structural borders combined with tinted ambient illumination, avoiding murky drop-shadows:

- **Surface Grounding**: Base canvas sits flat at `#F8FAFC`. Floating question blocks, filter bars, and modal sheets are hoisted via crisp `#FFFFFF` surfaces rimmed by an ultra-fine border: `1px solid #E2E8F0`.
- **Level 1 (Cards, Feed Items, Keyboards)**:
  `box-shadow: 0 1px 3px 0 rgba(15, 23, 42, 0.04), 0 1px 2px -1px rgba(15, 23, 42, 0.02);`
- **Level 2 (Active Quizzes, Sticky Filter Bars, Scan Reticles)**:
  `box-shadow: 0 8px 16px -4px rgba(79, 70, 229, 0.08), 0 4px 6px -2px rgba(15, 23, 42, 0.03);`
- **Level 3 (Modals, Overlays, Rank Up Celebrations)**:
  `box-shadow: 0 20px 25px -5px rgba(15, 23, 42, 0.1), 0 8px 10px -6px rgba(15, 23, 42, 0.05);`
- **Luminescent Accent States**: Active state nodes, streak triggers, and camera lock indicators apply a tight 6px directional colored glow (`rgba(79, 70, 229, 0.25)` or `rgba(16, 185, 129, 0.25)`) to signal focus without occluding nearby text.

## Shapes

The interface utilizes a disciplined rounded-corner profile (`level 2`) to balance high-efficiency tooling with approachable consumer energy:

- **Input Fields, Chips, and Buttons**: Fixed radius of `0.5rem` (8px) for buttons and inputs; outer card containers, exam sheets, and bottom sheets step up to `rounded-lg` (`1rem` / 16px) or `rounded-xl` (`1.5rem` / 24px).
- **Interactive Badges & Filters**: Completely circular or pill-shaped for state pills and tag badges to distinguish them instantly from rectangular question cards.
- **Scanner Viewfinder**: High-precision boundary brackets utilize a 12px outer radius with inset guide markers to emphasize instant optical alignment.

## Components

### Buttons
- **Primary Action (Scan / Submit)**: Solid `#4F46E5` background, `#FFFFFF` text, `0.5rem` border radius. Height: 48px. Includes a subtle bottom inset accent (`box-shadow: inset 0 -2px 0 0 #3730A3`) for tactile depth.
- **Secondary (Step Reveal / Bookmark)**: Clean `#FFFFFF` fill, 1px `#E2E8F0` border, `#0F172A` text. Hover/pressed state shifts to `#F8FAFC`.
- **Tertiary / Utility**: Ghost styling with `#4F46E5` text, zero elevation, active background tint `#EEF2FF`.

### Question & LaTeX Display Cards
- High-priority surfaces styled in pure `#FFFFFF` with `rounded-lg` borders.
- Top meta bar displays chapter identifiers, difficulty dots (Emerald for Easy, Amber for Intermediate, Electric Violet for Advanced), and copy/share quick triggers.
- LaTeX formula containers render inside an insulated inset background (`#F1F5F9`) with horizontal scrolling enabled for wide equations.

### Filter Pills & Segmented Controls
- Segmented pills live in a shallow `#F1F5F9` track.
- Active pill transitions with a sharp `#FFFFFF` chip elevated by Level 1 shadow, featuring vibrant `#0F172A` text and bold JetBrains Mono hit counters.

### Stats Chips & Glow Badges
- **Status Chips**: Inline micro-badges using a soft 10% tint of the parent semantic color (e.g., Emerald bg with Emerald text for `98% ACCURACY`).
- **Glow Badges (Trophies & Streak Counters)**: Amber Gold (`#F59E0B`) background with soft ambient drop blur (`0 0 12px rgba(245, 158, 11, 0.3)`), paired with crisp label-caps typography.

### Progress Heatmap & Velocity Metrics
- Dense grid blocks (representing question review intervals / 30-day streak density).
- Four-step gradient progression: Level 0 (`#F1F5F9`), Level 1 (`#A7F3D0`), Level 2 (`#34D399`), Level 3 (`#059669`).
- Active day marker is bordered with a 2px `#4F46E5` indicator ring.

### Inputs & OCR Viewfinder
- **Text Inputs**: 44px height, `#FFFFFF` background, 1px `#CBD5E1` border. Focused state: 2px `#4F46E5` halo outline.
- **Viewfinder Reticle**: Centered camera overlay with a high-contrast inverted backdrop, featuring animated scanning sweeps in Electric Violet and tactile snap-haptic cues upon formula detection.

### Bottom Navigation Tabs
- Frosted floating nav bar with a translucent white background (`rgba(255, 255, 255, 0.85)`, `backdrop-filter: blur(12px)`).
- Centered elevated floating action button (FAB) for the camera question scanner, elevated via Level 2 shadow with high-contrast `#4F46E5` fill.