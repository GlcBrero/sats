/**
 * Copyright by Michael Weiss, weiss.michael@gmx.ch
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.spectrumauctions.sats.clt;

/**
 * @author Michael Weiss
 *
 */
public enum Model {

    BVM, MBVM, SRVM, MRVM, LSVM, GSVM, CATS;

    public static String allModels(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values().length; i++){
            builder.append(values()[i]);
            if(i != values().length-1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
