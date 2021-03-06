/**
 * Copyright by Michael Weiss, weiss.michael@gmx.ch
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.spectrumauctions.sats.core.model.srvm;

import org.spectrumauctions.sats.core.bidlang.generic.FlatSizeIterators.GenericSizeDecreasing;
import org.spectrumauctions.sats.core.bidlang.generic.FlatSizeIterators.GenericSizeIncreasing;
import org.spectrumauctions.sats.core.bidlang.generic.FlatSizeIterators.GenericSizeOrdered;
import org.spectrumauctions.sats.core.bidlang.generic.GenericValueBidder;
import org.spectrumauctions.sats.core.model.Bidder;
import org.spectrumauctions.sats.core.model.UnsupportedBiddingLanguageException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

/**
 * @author Michael Weiss
 *
 */
public class SizeOrderedGenericFactory {

    static BandComparator comparator = new BandComparator();

    public static GenericSizeOrdered<SRVMBand> getSizeOrderedGenericLang(boolean increasing, SRVMBidder bidder) throws UnsupportedBiddingLanguageException {
        Set<SRVMBand> bands = bidder.getWorld().getBands();
        if (increasing) {
            return new Increasing(bands, bidder);
        } else {
            return new Decreasing(bands, bidder);
        }
    }


    private static final class Increasing extends GenericSizeIncreasing<SRVMBand> {


        private final SRVMBidder bidder;

        protected Increasing(Collection<SRVMBand> allPossibleGenericDefintions, SRVMBidder bidder)
                throws UnsupportedBiddingLanguageException {
            super(allPossibleGenericDefintions);
            this.bidder = bidder;
        }

        @Override
        public Bidder<SRVMLicense> getBidder() {
            return bidder;
        }

        /**
         * @see GenericSizeOrdered#getGenericBidder()
         */
        @Override
        protected GenericValueBidder<SRVMBand> getGenericBidder() {
            return bidder;
        }

        /**
         * @see GenericSizeOrdered#getDefComparator()
         */
        @Override
        protected Comparator<SRVMBand> getDefComparator() {
            return comparator;
        }
    }

    private static final class Decreasing extends GenericSizeDecreasing<SRVMBand> {


        private final SRVMBidder bidder;

        protected Decreasing(Collection<SRVMBand> allPossibleGenericDefintions, SRVMBidder bidder)
                throws UnsupportedBiddingLanguageException {
            super(allPossibleGenericDefintions);
            this.bidder = bidder;
        }

        @Override
        public SRVMBidder getBidder() {
            return bidder;
        }

        /**
         * @see GenericSizeOrdered#getGenericBidder()
         */
        @Override
        protected GenericValueBidder<SRVMBand> getGenericBidder() {
            return bidder;
        }

        /**
         * @see GenericSizeOrdered#getDefComparator()
         */
        @Override
        protected Comparator<SRVMBand> getDefComparator() {
            return comparator;
        }
    }


    private static class BandComparator implements Comparator<SRVMBand>, Serializable {

        private static final long serialVersionUID = -7929466674087601381L;

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(SRVMBand o1, SRVMBand o2) {
            return o1.toString().compareTo(o2.toString());
        }

    }


}
