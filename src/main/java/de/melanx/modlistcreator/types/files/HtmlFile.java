package de.melanx.modlistcreator.types.files;

import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.minecraft.modpack.CurseModpack;
import com.therandomlabs.curseapi.project.CurseMember;
import com.therandomlabs.curseapi.project.CurseProject;
import de.melanx.modlistcreator.types.FileBase;

import java.io.File;

public class HtmlFile extends FileBase {

    public HtmlFile(CurseModpack pack, boolean detailed, boolean headless) {
        super(pack, detailed, headless);
    }

    @Override
    public void generateFile(String name, File output) {
        this.log("\u001B[31mPutting \u001B[32meverything \u001B[34mtogether\u001B[35m.\u001B[36m.\u001B[33m.\u001B[0m");
        if (!this.headless) {
            this.builder.append("<h2>");
            this.builder.append(this.getHeader());
            this.builder.append("</h2>");
            this.builder.append("\n\n");
        }

        this.projects.forEach(entry -> {
            this.builder.append("<li>");
            this.builder.append(this.getFormattedProject(entry.getProject(), entry.getFile()));
            this.builder.append(" (by ");
            this.builder.append(this.getFormattedAuthor(entry.getProject().author()));
            this.builder.append(")</li>\n");
        });

        this.generateFinalFile(name, output);
    }

    @Override
    protected String getFormattedProject(CurseProject project, CurseFile file) {
        try {
            return String.format("<a href=\"https://www.curseforge.com/minecraft/%s/%s%s\">%s</a>",
                    project.categorySection().asCategory().slug(),
                    project.slug(),
                    this.detailed ? "/files/" + file.id() : "",
                    this.detailed ? file.displayName() : project.name());
        } catch (CurseException e) {
            throw new IllegalStateException("Following file caused an error: " + file.toString(), e);
        }
    }

    @Override
    protected String getFormattedAuthor(CurseMember member) {
        return String.format("<a href=\"https://www.curseforge.com/members/%s/projects\">%s</a>", member.name().toLowerCase(), member.name());
    }

    @Override
    public String getExtension() {
        return "html";
    }
}
