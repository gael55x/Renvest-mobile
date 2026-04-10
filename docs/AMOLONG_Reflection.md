# AMOLONG_Reflection

This file is the Markdown version of `AMOLONG_Reflection.pdf`.

Working on the three selected Renvest screens made me realize that architecture is not just about making the project look organized, but also about making it easier to understand, explain, and continue building. One of the biggest challenges for me was knowing when the structure was already enough. At first, I thought adding more layers would make the project look more complete, but after a while it started to feel like simple features were becoming harder to explain than they should be. That was the main reason why simplifying the shared authentication logic into one `AuthStore` felt like the better decision for this project.

The difference between vertical slicing and horizontal layering also became clearer while I was working on the code. In a horizontal structure, files are grouped by type, so one feature can end up split across several folders. Personally, I found that more confusing because it breaks my focus when I am only trying to understand one screen. Vertical slicing felt more natural to me because everything related to `login`, `register`, or `dashboard` is grouped together. It made the project easier to read, easier to document, and easier to present.

I also found MVP helpful because it gave each part of the feature a clear role. The View handled the UI, the Presenter handled the screen logic, and the data side handled stored values and session state. In my opinion, that separation made debugging less stressful because I could narrow problems down faster. Overall, I feel that vertical slicing made the project easier to organize, while MVP made each screen easier to reason about.
