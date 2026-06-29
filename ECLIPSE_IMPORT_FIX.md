# Eclipse Import Fix

Your screenshot shows Eclipse imported `automation-tests\src` as a project named `src`. That is the wrong folder.

Use these steps:

1. In Eclipse Project Explorer, right-click the `src` project.
2. Click `Delete`.
3. Important: uncheck `Delete project contents on disk`, then confirm.
4. Close Eclipse.
5. Reopen Eclipse and choose a workspace folder that is not `automation-tests`.

Good workspace example:

```text
C:\Users\2492376\eclipse-workspace
```

6. Import the framework using:

```text
File > Import > Maven > Existing Maven Projects
```

7. Browse to:

```text
C:\Users\2492376\Downloads\mentormatch-main\mentormatch-main\automation-tests
```

8. Select `pom.xml`, then click `Finish`.

Do not use:

```text
File > Import > General > Projects from Folder or Archive
```

That wizard can accidentally import nested folders like `src`.

After import, right-click the project and run:

```text
Maven > Update Project
```

If dependencies fail with a PKIX/certificate error, add this to your Eclipse JDK default VM arguments:

```text
-Djavax.net.ssl.trustStoreType=Windows-ROOT
```
