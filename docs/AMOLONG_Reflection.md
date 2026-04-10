# AMOLONG_Reflection

This file is the Markdown version of `AMOLONG_Reflection.pdf`.

Developing the three selected Renvest screens showed that architecture needs to balance structure and simplicity. One of the main challenges was avoiding unnecessary layers while still following the MVP pattern clearly. At first, it was easy to add extra abstractions because they appeared more formal, but this quickly made the code more difficult to explain and maintain. Refactoring the shared authentication logic into a single `AuthStore` class made the project cleaner while still preserving the separation required by MVP.

The comparison between vertical slicing and horizontal layering also became clearer during implementation. In a horizontal structure, files are grouped by type, such as placing all Activities in one package and all Presenters in another. That approach can work, but it spreads a single feature across multiple directories. Vertical slicing was more effective for this project because each selected screen, namely login, register, and dashboard, is treated as one feature slice. As a result, the related files are easier to locate, read, and present.

MVP provided a practical structure for separating responsibilities. The View manages user interface rendering and user input, the Presenter handles screen behavior and decisions, and the Model/Data side manages stored values and session state. This made debugging more direct because issues could be traced to either the UI, the interaction logic, or the data class. Overall, vertical slicing improved feature organization, while MVP improved clarity of responsibility inside each slice.
