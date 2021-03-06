/**
 * Copyright by Michael Weiss, weiss.michael@gmx.ch
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.spectrumauctions.sats.core.bidfile;

import org.spectrumauctions.sats.core.bidlang.generic.GenericDefinition;
import org.spectrumauctions.sats.core.bidlang.generic.GenericLang;
import org.spectrumauctions.sats.core.bidlang.xor.CatsXOR;
import org.spectrumauctions.sats.core.bidlang.xor.XORLanguage;
import org.spectrumauctions.sats.core.bidlang.xor.XORValue;
import org.spectrumauctions.sats.core.model.Good;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class CatsExporter extends FileWriter {

    public CatsExporter(File path) {
        super(path);
    }

    @Override
    public File writeSingleBidderXOR(XORLanguage<? extends Good> valueFunction, int numberOfBids, String filePrefix) throws IOException {
        Iterator iter = valueFunction.iterator();
        Set<XORValue<?>> selectedValues = new HashSet<>();
        List<String> bidLines = fileInit(valueFunction);
        for (int i = 0; i < numberOfBids && iter.hasNext(); i++) {
            XORValue<?> value = (XORValue) iter.next();
            selectedValues.add(value);
            String line = String.valueOf(i).concat("\t");
            line = line.concat(roundedValue(value.value().doubleValue()));
            line = line.concat("\t");
            line = line.concat(value.getLicenses().itemIds("\t")).concat("#");
            bidLines.add(line);
        }
        List<String> lines = new ArrayList<>();
        lines.add("goods " + valueFunction.getBidder().getWorld().getNumberOfGoods());
        lines.add("bids " + selectedValues.size());
        lines.add("dummy 0");
        lines.add("");
        lines.addAll(bidLines);

        return write(lines, filePrefix);

    }

    private List<String> fileInit(XORLanguage<? extends Good> lang) {
        List<String> lines = new ArrayList<>();

        String satsversion = null;
        try {
            satsversion = getClass().getPackage().getImplementationVersion();
        }catch (Exception e){
            //Do Nothing
        }
        if(satsversion == null){
             satsversion = "(UNKNOWN VERSION)";
        }
        lines.add("%% File generated by SATS  ".concat(satsversion).concat("  on  ").concat(new Date().toString()));
        lines.add("");
        lines.add("%% The SATS webpage is http://spectrumauctions.org");
        lines.add("");
        return lines;
    }

    @Override
    public File writeMultiBidderXOR(Collection<XORLanguage<? extends Good>> valueFunctions, int numberOfBids, String filePrefix) throws IOException {
        List<String> lines = fileInit(valueFunctions.iterator().next());
        lines.add("%% This file may contain bids from multiple bidders.");
        lines.add("% Bids from different bidders are separated using dummy items with negative IDs");
        lines.add("");
        lines.add("");
        List<String> bidLines = new ArrayList<>();
        //Dummy items are negative integers, for easier distinction
        int dummyItem = -1;
        int countBids = 0;
        for (XORLanguage<? extends Good> valueFunction : valueFunctions) {
            Iterator iter = valueFunction.iterator();
            for (int i = 0; i < numberOfBids && iter.hasNext(); i++) {
                XORValue<?> value = (XORValue) iter.next();
                StringBuilder line = new StringBuilder(String.valueOf(countBids++)).append("\t");
                line.append(roundedValue(value.value().doubleValue()));
                line.append("\t");
                line.append(value.getLicenses().itemIds("\t"));
                line.append("\t");
                line.append(dummyItem);
                line.append("\t#");
                bidLines.add(line.toString());
            }
            dummyItem--;
        }

        lines.add("goods " + valueFunctions.iterator().next().getBidder().getWorld().getNumberOfGoods());
        lines.add("bids " + bidLines.size());
        lines.add("dummy " + valueFunctions.size());
        lines.add("");
        lines.addAll(bidLines);
        return write(lines, filePrefix);
    }

    private File write(List<String> lines, String filePrefix) throws IOException {
        Path file = nextNonexistingFile(filePrefix);
        Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
//            Files.write(file, lines, StandardOpenOption.CREATE); //jvm 8 version
        return file.toFile();
    }

    /* (non-Javadoc)
     * @see FileWriter#writeMultiBidderXORQ(java.util.Collection, int, java.lang.String)
     */
    @Override
    public File writeMultiBidderXORQ(Collection<GenericLang<GenericDefinition>> valueFunctions, int numberOfBids,
                                     String filePrefix) throws IOException {
        throw new UnsupportedOperationException("XOR-Q is not compatible with the CATS file format");
    }

    /* (non-Javadoc)
     * @see FileWriter#writeSingleBidderXORQ(GenericLang, int, java.lang.String)
     */
    @Override
    public File writeSingleBidderXORQ(GenericLang<GenericDefinition> lang, int numberOfBids, String filePrefix)
            throws IOException {
        throw new UnsupportedOperationException("XOR-Q is not compatible with the CATS file format");
    }

    /* (non-Javadoc)
     * @see FileWriter#filetype()
     */
    @Override
    protected String filetype() {
        return "txt";
    }

}
