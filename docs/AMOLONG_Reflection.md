# AMOLONG_Reflection

This file is the Markdown version of `AMOLONG_Reflection.pdf`.

Working on Renvest made me realize that keeping the architecture clean is harder than it looks at first. One challenge was trying to balance organization and simplicity. At the start, it was tempting to keep adding more structure because it felt more “professional,” but after a while I noticed that too much structure can also slow things down. If every small screen has too many moving parts, the code starts to feel heavier than the actual feature. That was something I had to be careful about while still following the required MVP format.

Comparing vertical slicing and horizontal layering also helped me understand architecture better. In a horizontal structure, files are grouped by type, like putting all Activities together, all Presenters together, and all Models together. That can work, but it becomes annoying once the project grows because one feature gets scattered across many folders. With vertical slicing, each feature keeps its related files together, so it is easier to trace what is happening in one screen. For me, that setup feels more natural because I can focus on one feature at a time without getting lost.

MVP was also useful because it gave each part of the app a clear responsibility. The View handles UI, the Presenter handles decisions and interactions, and the Model deals with data. That separation made the code easier to read and explain. It also made debugging less messy because I could quickly figure out whether the problem was in the UI, the logic, or the data part. Overall, MVP plus vertical slicing gave the project better structure, but it also taught me that architecture should stay clear and practical, not just look complicated.
