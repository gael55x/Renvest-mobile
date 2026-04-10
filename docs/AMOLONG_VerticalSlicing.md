# AMOLONG_VerticalSlicing

This file is the Markdown version of `AMOLONG_VerticalSlicing.pdf`.

Vertical slicing architecture means the project is grouped by feature or screen instead of grouping everything by technical layer first. So instead of putting all Activities in one folder, all Presenters in another folder, and all Models somewhere else, each feature keeps its own files together. In Renvest, that means `login`, `dashboard`, `customers`, `promotions`, and other screens each have their own package. For me, this setup makes more sense because when I open one feature, I can already see the UI, the presenter logic, and the related model/data flow in one place.

I think vertical slicing is helpful because it makes the code easier to read and maintain, especially in a student project or an MVP app. If I need to fix the login flow, I do not have to jump across a bunch of unrelated folders. Everything connected to login is already close together. It also helps avoid confusion when the app gets bigger, because every screen has clear boundaries. Shared stuff like repositories can still stay in `data`, but the feature package is still the main entry point. For Renvest, this structure is clean, practical, and easier to explain during submission or demo day.
