---
name: Artisanal Warmth
colors:
  surface: '#fff8f5'
  surface-dim: '#e1d8d4'
  surface-bright: '#fff8f5'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#fbf2ed'
  surface-container: '#f5ece7'
  surface-container-high: '#efe6e2'
  surface-container-highest: '#e9e1dc'
  on-surface: '#1e1b18'
  on-surface-variant: '#504539'
  inverse-surface: '#34302c'
  inverse-on-surface: '#f8efea'
  outline: '#837567'
  outline-variant: '#d5c4b4'
  surface-tint: '#825516'
  primary: '#825516'
  on-primary: '#ffffff'
  primary-container: '#d9a05b'
  on-primary-container: '#5c3700'
  inverse-primary: '#f8bb73'
  secondary: '#465f88'
  on-secondary: '#ffffff'
  secondary-container: '#b6d0ff'
  on-secondary-container: '#3f5881'
  tertiary: '#645e49'
  on-tertiary: '#ffffff'
  tertiary-container: '#b2ab92'
  on-tertiary-container: '#443f2c'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffddb9'
  primary-fixed-dim: '#f8bb73'
  on-primary-fixed: '#2b1700'
  on-primary-fixed-variant: '#663e00'
  secondary-fixed: '#d6e3ff'
  secondary-fixed-dim: '#aec7f7'
  on-secondary-fixed: '#001b3d'
  on-secondary-fixed-variant: '#2e476f'
  tertiary-fixed: '#ebe2c8'
  tertiary-fixed-dim: '#cec6ad'
  on-tertiary-fixed: '#1f1c0b'
  on-tertiary-fixed-variant: '#4c4733'
  background: '#fff8f5'
  on-background: '#1e1b18'
  surface-variant: '#e9e1dc'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Work Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Work Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Work Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  caption:
    fontFamily: Work Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-max: 1280px
  gutter: 24px
  margin-desktop: 64px
  margin-mobile: 20px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 32px
---

## Brand & Style
The brand personality for the design system is anchored in the concept of "The Professional Baker"—combining the warmth of a community bakery with the industrial reliability of a large-scale shop system. It evokes an emotional response of comfort, appetite, and unwavering trust.

The design style is a hybrid of **Corporate Modern** and **Tactile Minimalism**. It utilizes generous whitespace to allow high-fidelity food photography to breathe, while employing soft, approachable UI elements that mimic the organic nature of dough and baking. The visual language avoids cold, sharp edges in favor of a "baked" softness that feels premium yet accessible.

## Colors
The palette is divided into two emotional pillars: **Earth** (Warmth) and **Foundation** (Trust).

- **Primary (Wheat Yellow/Gold):** Used for key actions and brand accents, representing the golden crust of a fresh loaf.
- **Secondary (Deep Navy):** Used for navigation, headers, and professional elements to establish authority and reliability.
- **Tertiary (Cream/Dough):** Used for large background surfaces and container fills to soften the UI.
- **Neutral (Charcoal):** Reserved for high-readability text and subtle borders.
- **Functional Colors:** Desaturated versions of green, red, and orange are used for feedback messages to maintain the organic, natural feel of the brand.

## Typography
This design system uses a two-font strategy. **Plus Jakarta Sans** provides a friendly, modern, and slightly rounded geometric feel for headlines, reinforcing the "warm" brand personality. **Work Sans** is used for all body and functional text; its grounded and professional character ensures maximum legibility for ingredient lists, pricing, and administrative data.

Headlines should use tighter letter-spacing to appear more cohesive, while body text maintains standard spacing for a professional, news-like clarity.

## Layout & Spacing
The layout follows a **Fluid Grid** system based on an 8px base unit. 

- **Desktop:** A 12-column grid with 24px gutters. Use large 64px outer margins to create a "boutique" feel.
- **Tablet:** 8-column grid with 20px gutters.
- **Mobile:** 4-column grid with 16px gutters.

Spacing between related items (like an image and its label) should use `stack-sm`. Spacing between distinct sections within a card should use `stack-md`. Entire page sections must be separated by `stack-lg` to maintain the "clean whitespace" aesthetic.

## Elevation & Depth
Depth is created through **Tonal Layers** and **Ambient Shadows**. 

Instead of harsh black shadows, this design system uses soft, diffused shadows tinted with the brand's Deep Navy or Bread Brown to keep the interface feeling warm.
- **Level 1 (Cards):** Very soft blur (12px), 5% opacity primary color tint. No visible border.
- **Level 2 (Dropdowns/Modals):** Medium blur (24px), 10% opacity neutral tint.
- **Interactive State:** Elements should "lift" on hover, increasing shadow blur and slightly shifting Y-offset.

## Shapes
The shape language is defined by **Rounded** corners. 

A standard 8px (`0.5rem`) radius is used for small components like buttons and inputs. Large containers, such as product cards and modals, use `rounded-lg` (16px / `1rem`) to emphasize the soft, approachable nature of the baking industry. Circular shapes are reserved strictly for avatars and icon buttons.

## Components

### Buttons
- **Primary:** Solid `primary_color_hex` with white or dark-neutral text. 16px vertical padding.
- **Secondary:** Outlined with `secondary_color_hex`, 2px border width.
- **Tertiary:** Ghost style, using `secondary_color_hex` for text only.

### Inputs
- Backgrounds should be a very light tint of `tertiary_color_hex` (5% opacity) to feel less "sterile" than pure white.
- 2px border on focus using `primary_color_hex`.
- Labels are always `label-md` and placed above the field.

### Cards
- Use `rounded-lg` corners.
- Padding should be generous (24px) to ensure a high-fidelity, premium look.
- Images in cards should have no top radius if they are full-bleed to the edge.

### Feedback Messages
- **Structure:** A soft-tinted background of the functional color (10% opacity) with a solid 4px left-border of the full-strength functional color.
- Icons should always accompany the text to ensure accessibility and professional clarity.

### Chips/Tags
- Used for categories (e.g., "Sourdough", "Vegan").
- Use `pill-shaped` (100px) radius and desaturated versions of the secondary color.