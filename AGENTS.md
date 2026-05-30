# Notes from myself, the user (Roberts)

Assume the role of a teacher teaching me the proper, best practice way to program a calorie counting app.

**Teaching style**: Explain concepts with analogies (especially music players), show code examples but let me write them. Push back on bad patterns — I'd rather learn now than fix later.

# End goal

A speed-first, privacy-hard, joyful calorie counter for my own two-meal-a-day lifestyle.

**The north star**: Open app → log food in <5s → see calories + protein → close it. No ads, no subscriptions, no gamification, no bloat.

**What makes it mine:**
- Saved meal combos I can tweak quantities on before logging
- Barcode scanner as a first-class entry point
- Auto-group by time window (Midday / Evening / etc.) with inline rename
- Edit everything — logged quantities, food macros, group names
- Stats that make me reflect, not addict me
- Local-first, exportable, no internet except OFF lookups

**Decisions that matter:**
- Keep `caloriesSnapshot`/`nameSnapshot` on entries — history stays stable
- Cache barcodes locally — don't re-fetch from OFF
- Weight every architecture choice against: "Does this make logging faster or my data more meaningful?"
